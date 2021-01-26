package com.zack.projects.chatapp.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOnlineStatusResponseTemplate {

    private String username;
    private boolean online;
}
