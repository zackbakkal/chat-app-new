package com.zack.projects.chatapp.controller;

import com.zack.projects.chatapp.entity.User;
import com.zack.projects.chatapp.entity.UserConversation;
import com.zack.projects.chatapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class ChatappTemplateController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("chatapp")
    public String getChatapp(Model model) {

        List<User> offlineUsers = userRepository
                .findAll()
                .stream()
                .filter(user ->
                        !user.isOnline())
                .collect(Collectors.toList());

        List<User> onlineUsers = userRepository
                .findAll()
                .stream()
                .filter(user ->
                        user.isOnline())
                .collect(Collectors.toList());

        System.out.println(offlineUsers);

        model.addAttribute("offlineusers", offlineUsers);
        model.addAttribute("onlineusers", onlineUsers);

        return "chatapp";
    }

    @GetMapping
    public void getConversation(Model model) {
        System.out.println(model.getAttribute("username"));
    }
}
