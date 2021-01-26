package com.zack.projects.chatapp.controller;

import com.zack.projects.chatapp.VO.MessageResponseTemplate;
import com.zack.projects.chatapp.entity.Message;
import com.zack.projects.chatapp.exception.UserNameNotFoundException;
import com.zack.projects.chatapp.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
@Slf4j
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public MessageResponseTemplate sendMessage(@RequestBody Message message) throws UserNameNotFoundException {
        log.info(String.format("Sending request to sendMessage in messageService"));
        return messageService.sendMessage(message);
    }

}
