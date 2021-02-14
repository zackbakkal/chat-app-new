package com.zack.projects.chatapp.controller;

import com.zack.projects.chatapp.VO.MessageRequestTemplate;
import com.zack.projects.chatapp.VO.MessageResponseTemplate;
import com.zack.projects.chatapp.entity.*;
import com.zack.projects.chatapp.exception.UserNameNotFoundException;
import com.zack.projects.chatapp.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

@RestController
@RequestMapping("/messages")
@Slf4j
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private NotificationController notificationController;

    @PostMapping("/send")
    public MessageResponseTemplate sendMessage(@RequestBody Message message)
            throws UserNameNotFoundException {
        log.info(String.format("Sending request to sendMessage in messageService"));
        return messageService.sendMessage(message);
    }

    @PostMapping("/sendMessage")
    public MessageResponseTemplate sendMessage2(
            @RequestBody MessageRequestTemplate messageRequestTemplate,
            HttpServletRequest httpServletRequest)
            throws UserNameNotFoundException {

        log.info(String.format("Sending request to sendMessage in messageService"));

        Message message = new Message();

        message.setText(messageRequestTemplate.getText());

        String sender = httpServletRequest.getRemoteUser();

        SenderRecipient senderRecipient = new SenderRecipient(sender, messageRequestTemplate.getRecipient());

        message.setSenderRecipient(senderRecipient);

        log.info(String.format("Setting message sent date"));
        message.setDateSent(new Timestamp(System.currentTimeMillis()));

        notificationController.deliverMessage(message);

        return messageService.sendMessage(message);
    }

}
