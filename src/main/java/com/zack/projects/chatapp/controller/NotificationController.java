package com.zack.projects.chatapp.controller;

import com.zack.projects.chatapp.entity.Message;
import com.zack.projects.chatapp.exception.UserNameNotFoundException;
import com.zack.projects.chatapp.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/notifications")
@Slf4j
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("subscribe")
    public SseEmitter subscribe(HttpServletRequest httpServletRequest) {
        String username = httpServletRequest.getRemoteUser();
        return notificationService.subscribeUser(username);
    }

    public void deliverMessage(Message message) throws UserNameNotFoundException {
        notificationService.deliverMessage(message);
    }

    public void deliverMissedMessage(String recipient) {
        notificationService.deliverMissedMessages(recipient);
    }

    public void updateStatusUsersList(String username, boolean status) throws UserNameNotFoundException {
        notificationService.updateStatusUsersList(username, status);
    }

    public void updateAvailabilityUsersList(String username, String availability) {
        notificationService.updateAvailabilityUsersList(username, availability);
    }

    public void notificationReceived(String sender, String recipient) {
        notificationService.notificationReceived(sender, recipient);
    }

    public void updateUserAvatar(String username) throws UserNameNotFoundException {
        notificationService.updateUserAvatar(username);
    }

}
