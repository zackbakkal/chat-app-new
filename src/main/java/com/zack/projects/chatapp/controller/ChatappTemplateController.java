package com.zack.projects.chatapp.controller;

import com.zack.projects.chatapp.VO.ProfileResponseTemplate;
import com.zack.projects.chatapp.entity.User;
import com.zack.projects.chatapp.exception.UserNameNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/chatapp")
@Slf4j
public class ChatappTemplateController {

    @Autowired
    private NotificationController notificationController;

    @Autowired
    private UserController userController;

    @GetMapping
    public String getChatapp(Model model,
                             HttpServletRequest httpServletRequest) throws UserNameNotFoundException {

        ProfileResponseTemplate profile = userController.getUserProfile(httpServletRequest.getRemoteUser());


        model.addAttribute("user", profile);
        return "chatapp";
    }

}
