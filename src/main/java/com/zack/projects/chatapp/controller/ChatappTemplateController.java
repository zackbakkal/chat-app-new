package com.zack.projects.chatapp.controller;

import com.zack.projects.chatapp.entity.Message;
import com.zack.projects.chatapp.entity.User;
import com.zack.projects.chatapp.service.ChatappTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/chatapp")
public class ChatappTemplateController {

    @Autowired
    private ChatappTemplateService chatappTemplateService;

    private List<Message> messages;

    @GetMapping("/")
    public String getChatapp(Model model) {

        List<User> offlineUsers = chatappTemplateService.getOfflineUsers();
        List<User> onlineUsers = chatappTemplateService.getOnlineUsers();

        model.addAttribute("offlineusers", offlineUsers);
        model.addAttribute("onlineusers", onlineUsers);
        model.addAttribute("conversation", messages);

        return "chatapp";
    }

    @GetMapping("conversations/{username}")
    public String getConversation(
            @PathVariable String username,
            Model model,
            final RedirectAttributes redirectAttributes) {

        System.out.println(username);

        String owner = "zack";
//                httpServletRequest.getRemoteUser();
        messages = chatappTemplateService.getConversation(owner, username);
        System.out.println(messages);
        redirectAttributes.addAttribute("conversation", messages);

        return getChatapp(model);
    }
}
