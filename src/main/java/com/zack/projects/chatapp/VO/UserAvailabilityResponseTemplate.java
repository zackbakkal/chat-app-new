package com.zack.projects.chatapp.VO;

import com.zack.projects.chatapp.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAvailabilityResponseTemplate {

    private String username;
    private String availability;

    public UserAvailabilityResponseTemplate(User user) {
        this.username = user.getUsername();
        this.availability = user.getAvailability();
    }

}
