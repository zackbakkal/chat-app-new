package com.zack.projects.chatapp.amazon.bucket;

public enum BucketName {

    // AWS S3 bucket name
    PROFILE_IMAGE("chatapp-profile-image-upload");

    private final String bucketName;

    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }

}
