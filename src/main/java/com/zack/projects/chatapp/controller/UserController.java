package com.zack.projects.chatapp.controller;

import com.zack.projects.chatapp.VO.ProfileResponseTemplate;
import com.zack.projects.chatapp.VO.UserOnlineStatusResponseTemplate;
import com.zack.projects.chatapp.VO.UserAvailabilityResponseTemplate;
import com.zack.projects.chatapp.entity.User;
import com.zack.projects.chatapp.exception.UserNameExistsException;
import com.zack.projects.chatapp.exception.UserNameNotFoundException;
import com.zack.projects.chatapp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/")
    public ProfileResponseTemplate addUser(@RequestBody User user) throws UserNameExistsException {

        log.info(String.format("Calling UserService to save user [%s]", user));
        return userService.saveUser(user);
    }

    @GetMapping("/profile/{username}")
    public ProfileResponseTemplate getUserProfile(@PathVariable String username) throws UserNameNotFoundException {
        log.info(String.format("Calling UserService to get user [%s]", username));
        return userService.getUserProfile(username);
    }

    @GetMapping("/{username}")
    public UserOnlineStatusResponseTemplate searchUser(@PathVariable String username)
            throws UserNameNotFoundException {
        log.info(String.format("Calling UserService to get user [%s]", username));
        return userService.searchUser(username);
    }

    @GetMapping("/startwith/{username}")
    public List<UserOnlineStatusResponseTemplate> searchUsersStartWith(@PathVariable String username)
            throws UserNameNotFoundException {
        log.info(String.format("Calling UserService to get user [%s]", username));
        return userService.searchUsersStartWith(username);
    }

    @GetMapping("/all")
    public List<ProfileResponseTemplate> getUsers() {
        log.info(String.format("Calling UserService to get all user"));
        return userService.getUsersProfiles();
    }

    @GetMapping("/{username}/onlineStatus")
    public UserOnlineStatusResponseTemplate getUserOnlineStatus(@PathVariable String username)
            throws UserNameNotFoundException {
        log.info(String.format("Calling UserService to get user [%s] status", username));
        return userService.getUserOnlineStatus(username);
    }

    @GetMapping("/{username}/availability")
    public UserAvailabilityResponseTemplate getUserAvailability(@PathVariable String username)
            throws UserNameNotFoundException {
        log.info(String.format("Calling UserService to get user [%s] availability", username));
        return userService.getUserAvailability(username);
    }

    @PutMapping("/availability/{availability}")
    public UserAvailabilityResponseTemplate setUserAvailability(
            @PathVariable String availability,
            HttpServletRequest httpServletRequest) throws UserNameNotFoundException {

        String username = httpServletRequest.getRemoteUser();
        log.info(String.format("Calling UserService to set user [%s] availability", username));
        return userService.setUserAvailability(username, availability);
    }

    @PutMapping("/{username}/online")
    public UserOnlineStatusResponseTemplate setUserOnline(@PathVariable String username)
            throws UserNameNotFoundException {
        log.info(String.format("Calling UserService to set user [%s] status to online", username));
        return userService.setUserOnline(username);
    }

    @PutMapping("/{username}/offline")
    public UserOnlineStatusResponseTemplate setUserOffline(@PathVariable String username)
            throws UserNameNotFoundException {
        log.info(String.format("Calling UserService to set user [%s] status to offline", username));
        return userService.setUserOffline(username);
    }

    @GetMapping("/online")
    public List<UserOnlineStatusResponseTemplate> getOnlineUsers() {
        return userService.getOnlineUsers();
    }

    @GetMapping("/offline")
    public List<UserOnlineStatusResponseTemplate> getOfflineUsers() {
        return userService.getOfflineUsers();
    }

    @PutMapping("/update")
    public void updateProfile(
            @RequestBody ProfileResponseTemplate updatedProfile) throws UserNameNotFoundException {

        userService.updateProfile(updatedProfile);

    }

}
