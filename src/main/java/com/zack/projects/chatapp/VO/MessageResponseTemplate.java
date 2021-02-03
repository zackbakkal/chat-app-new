package com.zack.projects.chatapp.VO;

import com.zack.projects.chatapp.entity.Message;
import com.zack.projects.chatapp.entity.SenderRecipient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseTemplate {

    private SenderRecipient senderRecipient;
    private String text;
    private Timestamp dateSent;

    public MessageResponseTemplate(Message message) {
        this.senderRecipient = message.getSenderRecipient();
        this.text = message.getText();
        this.dateSent = message.getDateSent();
    }

    public MessageResponseTemplate(MessageNotificationTemplate messageNotificationTemplate) {
        this.senderRecipient.setRecipient(messageNotificationTemplate.getRecipient());
        this.senderRecipient.setSender(messageNotificationTemplate.getSender());
    }

}
