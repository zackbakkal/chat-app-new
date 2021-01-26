package com.zack.projects.chatapp.controller;

import com.zack.projects.chatapp.VO.ProfileResponseTemplate;
import com.zack.projects.chatapp.VO.UserOnlineStatusResponseTemplate;
import com.zack.projects.chatapp.entity.User;
import com.zack.projects.chatapp.exception.UserNameExistsException;
import com.zack.projects.chatapp.exception.UserNameNotFoundException;
import com.zack.projects.chatapp.service.ChatappUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private ChatappUserService chatappUserService;

    @PostMapping("/")
    public ProfileResponseTemplate addUser(@RequestBody User user) throws UserNameExistsException {

        log.info(String.format("Calling UserService to save user [%s]", user));
        return chatappUserService.saveUser(user);
    }

    @GetMapping("/{username}")
    public ProfileResponseTemplate getUser(@PathVariable String username) throws UserNameNotFoundException {
        log.info(String.format("Calling UserService to get user [%s]", username));
        return chatappUserService.getUserProfile(username);
    }

    @GetMapping("/all")
    public List<ProfileResponseTemplate> getUsers() {
        log.info(String.format("Calling UserService to get all user"));
        return chatappUserService.getUsersProfiles();
    }

    @GetMapping("/{username}/status")
    public UserOnlineStatusResponseTemplate getUserStatus(@PathVariable String username)
            throws UserNameNotFoundException {
        log.info(String.format("Calling UserService to get user [%s] status", username));
        return chatappUserService.getUserStatus(username);
    }

    @PutMapping("/{username}/online")
    public UserOnlineStatusResponseTemplate setUserOnline(@PathVariable String username)
            throws UserNameNotFoundException {
        log.info(String.format("Calling UserService to set user [%s] status to online", username));
        return chatappUserService.setUserOnline(username);
    }

    @PutMapping("/{username}/offline")
    public UserOnlineStatusResponseTemplate setUserOffline(@PathVariable String username)
            throws UserNameNotFoundException {
        log.info(String.format("Calling UserService to set user [%s] status to offline", username));
        return chatappUserService.setUserOffline(username);
    }

}
