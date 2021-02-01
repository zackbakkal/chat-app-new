package com.zack.projects.chatapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/chatapp")
public class ChatappTemplateController {

    @GetMapping
    public String getChatapp() {
        return "chatapp";
    }

}
