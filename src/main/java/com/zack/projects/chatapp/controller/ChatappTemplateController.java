package com.zack.projects.chatapp.controller;

import com.zack.projects.chatapp.entity.Message;
import com.zack.projects.chatapp.entity.SenderRecipient;
import com.zack.projects.chatapp.entity.User;
import com.zack.projects.chatapp.exception.UserNameNotFoundException;
import com.zack.projects.chatapp.service.ChatappTemplateService;
import com.zack.projects.chatapp.service.MessageService;
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

    @Autowired
    private MessageService messageService;

    private List<Message> messages;

    private String currentRecipient;
    private String owner;

    @GetMapping("/")
    public String getChatapp(Model model) {

        List<User> offlineUsers = chatappTemplateService.getOfflineUsers();
        List<User> onlineUsers = chatappTemplateService.getOnlineUsers();

        model.addAttribute("offlineusers", offlineUsers);
        model.addAttribute("onlineusers", onlineUsers);
        model.addAttribute("conversation", messages);
        model.addAttribute("text", new String());

        return "chatapp";
    }

    @GetMapping("conversations/{username}")
    public String getConversation(
            @PathVariable String username,
            Model model,
            final RedirectAttributes redirectAttributes) {

        currentRecipient = username;

        owner = "zack";
//                httpServletRequest.getRemoteUser();
        messages = chatappTemplateService.getConversation(owner, username);
        System.out.println(messages);
        redirectAttributes.addAttribute("conversation", messages);

        return getChatapp(model);
    }

    @PostMapping("sendMessage")
    public String sendMessage(
            @ModelAttribute("text") String text,
            Model model) throws UserNameNotFoundException {

        Message message = new Message();
        message.setSenderRecipient(new SenderRecipient(owner, currentRecipient));
        message.setText(text);
        messageService.sendMessage(message);
        messages = chatappTemplateService.getConversation(owner, currentRecipient);

        System.out.println(messages);

        return getChatapp(model);
    }

}
