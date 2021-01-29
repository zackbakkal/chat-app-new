package com.zack.projects.chatapp.security;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

//	private ObjectMapper objectMapper = new ObjectMapper();

	private String error;

	public CustomAuthenticationFailureHandler() {
		super();
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		Map<String, Object> data = new HashMap<>();
		data.put("timestamp", Calendar.getInstance().getTime());
		data.put("exception", exception.getMessage());

		this.error = exception.getMessage();

//		response.getOutputStream().println(objectMapper.writeValueAsString(data));
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
