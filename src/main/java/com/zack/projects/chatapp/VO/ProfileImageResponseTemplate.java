package com.zack.projects.chatapp.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileImageResponseTemplate {

    private String username;
    private String profileImageUrl;

    public ProfileImageResponseTemplate(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}

