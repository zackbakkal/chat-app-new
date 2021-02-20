package com.zack.projects.chatapp.VO;

import com.zack.projects.chatapp.amazon.bucket.BucketName;
import com.zack.projects.chatapp.amazon.filestore.service.ImageStoreService;
import com.zack.projects.chatapp.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponseTemplate {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String profileImageUrl;

    public ProfileResponseTemplate(User user) {
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.profileImageUrl = ImageStoreService.buildProfileImageUrl(username, user.getProfileImageName());
    }

}
