package com.zack.projects.chatapp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/chatapp")
@Slf4j
public class ChatappTemplateController {

    @Autowired
    private NotificationController notificationController;

    @GetMapping
    public String getChatapp() {

        return "chatapp";
    }

}
