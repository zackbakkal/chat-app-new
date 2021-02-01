package com.zack.projects.chatapp.VO;

import com.zack.projects.chatapp.entity.Message;
import com.zack.projects.chatapp.entity.SenderRecipient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequestTemplate {

    private String recipient;
    private String text;

}
