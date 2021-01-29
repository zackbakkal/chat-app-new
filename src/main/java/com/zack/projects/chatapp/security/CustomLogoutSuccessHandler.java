package com.zack.projects.chatapp.security;

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

    @SneakyThrows
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info(String.format("Logging out user [%s]", authentication.getName()));
        userService.setUserOffline(authentication.getName());

        log.info(String.format("Logout successful with user [%s]", authentication.getName()));

        response.setStatus(HttpServletResponse.SC_OK);
        log.info(String.format("Redirecting to login page"));
        response.sendRedirect("/login");
    }
}
