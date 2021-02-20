package com.zack.projects.chatapp.VO;

import com.zack.projects.chatapp.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvatarNotoficationTemplate {

    private String username;
    private boolean hasAvatar;

    public AvatarNotoficationTemplate(User user) {
        this.username = user.getUsername();
        this.hasAvatar = user.getProfileImageName() != null;
    }

}
