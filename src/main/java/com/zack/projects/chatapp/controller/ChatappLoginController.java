package com.zack.projects.chatapp.controller;

import com.zack.projects.chatapp.entity.User;
import com.zack.projects.chatapp.exception.UserNameExistsException;
import com.zack.projects.chatapp.exception.UserNameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/")
public class ChatappLoginController {

    @Autowired
    private UserController userController;

    // login
    @GetMapping("login")
    public String getLoginView() {

        return "login";

    }

    // login-error
    @GetMapping("login-error")
    public String getLoginErrorView(Model model) {

        model.addAttribute("loginError", true);
        return "login";

    }

    // Open registration form
    @GetMapping("register")
    public String openRegistrationform(Model model) {

        model.addAttribute("user", new User());
        return "register";

    }

    @PostMapping("register")
    public String registerUser(
            @ModelAttribute("user") User user,
            final RedirectAttributes redirectAttributes) throws UserNameExistsException {

        redirectAttributes.addAttribute("user", user);

        return "registersuccessful";

    }

    @PutMapping("logout")
    public String logout(HttpServletRequest httpServletRequest) throws UserNameNotFoundException {

        System.out.println("loging out: " + httpServletRequest.getRemoteUser());
        userController.setUserOffline(httpServletRequest.getRemoteUser());

        return "login";
    }

}
