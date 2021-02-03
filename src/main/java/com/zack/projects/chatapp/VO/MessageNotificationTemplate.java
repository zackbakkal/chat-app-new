package com.zack.projects.chatapp.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageNotificationTemplate {

    private String sender;
    private String recipient;
    private String jsessionId;

}
