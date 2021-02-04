package com.zack.projects.chatapp.controller;

import com.zack.projects.chatapp.entity.Message;
import com.zack.projects.chatapp.exception.UserNameNotFoundException;
import com.zack.projects.chatapp.service.MessageNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/notifications")
@Slf4j
public class MessageNotificationController {

    @Autowired
    private MessageNotificationService messageNotificationService;

    @GetMapping("subscribe")
    public SseEmitter subscribe(HttpServletRequest httpServletRequest) {
        String username = httpServletRequest.getRemoteUser();
        return messageNotificationService.subscribeUser(username);
    }

    public void deliverMessage(Message message) throws UserNameNotFoundException {
        messageNotificationService.deliverMessage(message);
    }

    public void deliverMissedMessage(String recipient) {
        messageNotificationService.deliverMissedMessages(recipient);
    }

    public void updateUsersList(String username, boolean status) {
        messageNotificationService.updateUsersList(username, status);
    }

    public void notificationReceived(String sender, String recipient) {
        messageNotificationService.notificationReceived(sender, recipient);
    }

}
