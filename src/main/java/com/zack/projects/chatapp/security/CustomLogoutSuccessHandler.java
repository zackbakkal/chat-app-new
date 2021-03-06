package com.zack.projects.chatapp.security;

import com.zack.projects.chatapp.controller.NotificationController;
import com.zack.projects.chatapp.service.UserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationController notificationController;

    @SneakyThrows
    @Override
    public void onLogoutSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        log.info(String.format("Logging out user [%s]", authentication.getName()));
        userService.setUserOffline(authentication.getName());

        log.info(String.format("Logout successful with user [%s]", authentication.getName()));
        response.setStatus(HttpServletResponse.SC_OK);

        // TODO: Fix update users list. After the user logs out it disappear from the offline users.
        log.info("Notify all users");
        notificationController.updateStatusUsersList(authentication.getName(), false);

        log.info(String.format("Redirecting to login page"));
        response.sendRedirect("/login");

    }
}
