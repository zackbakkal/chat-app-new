package com.zack.projects.chatapp.dao;

import java.util.Optional;

import com.zack.projects.chatapp.auth.ApplicationUser;

public interface ApplicationUserDao {

	Optional<ApplicationUser> selectChatappApplicationUserByUserName(String username);
	
}
