package com.zack.projects.chatapp.amazon.filestore.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.zack.projects.chatapp.amazon.bucket.BucketName;
import com.zack.projects.chatapp.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static org.apache.http.entity.ContentType.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@Slf4j
public class ImageStoreService {

    @Autowired
    private final AmazonS3 s3;

    public ImageStoreService(AmazonS3 s3) {
        this.s3 = s3;
    }

    public String updateImage(User user, MultipartFile file) {

        isImage(file);

        Map<String, String> metadata = extractMetadata(file);

        String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), user.getUsername());
        String fileName = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID().toString());

        try {
            save(path, fileName, Optional.of(metadata), file.getInputStream());
            return fileName;
        } catch (IOException e) {
            throw new IllegalStateException("Something went wrong while uploading your file.");
        }

    }

    private Map<String, String> extractMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();

        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        return metadata;
    }

    private void isImage(MultipartFile file) {
        if(!Arrays.asList(
                IMAGE_JPEG.getMimeType(),
                IMAGE_PNG.getMimeType(),
                IMAGE_GIF.getMimeType(),
                IMAGE_SVG.getMimeType())
                .contains(file.getContentType())) {
            throw new IllegalStateException("Uploaded file is not a valid image file format.");
        }
    }

    public void save(
            String path,
            String filename,
            Optional<Map<String, String>> optionalMetadata,
            InputStream inputStream) {

        ObjectMetadata metadata = new ObjectMetadata();

        optionalMetadata.ifPresent(map -> {
            if(!map.isEmpty()) {
                map.forEach(metadata::addUserMetadata);
            }
        });

        try {
            log.info(String.format("Saving image [%s/%s]", path, filename));
            s3.putObject(path, filename, inputStream, metadata);
        } catch(AmazonServiceException e) {
            throw new IllegalStateException("Failed to upload image to sever.", e);
        }

    }

    public byte[] downloadProfileImage(User user) {

        System.out.println("Image Store Service: " + user);

        String filename = user.getProfileImageName();
        String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), user.getUsername());

        String profileImageUrl = buildProfileImageUrl(user.getUsername(), filename);

        try {
            log.info(String.format("Downloading image [%s/%s]", path, filename));
            S3Object object = s3.getObject(path, filename);
            S3ObjectInputStream inputStream = object.getObjectContent();

            return IOUtils.toByteArray(inputStream);

        } catch(AmazonServiceException |IOException e) {
            throw new IllegalStateException("Failed to download file from server.");
        }

    }

    public static String buildProfileImageUrl(String username, String profileImageName) {
        return String.format("%s/%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), username, profileImageName);
    }
}
