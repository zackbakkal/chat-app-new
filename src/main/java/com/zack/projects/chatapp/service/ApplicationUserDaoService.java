package com.zack.projects.chatapp.service;

import java.util.List;
import java.util.Optional;

import com.zack.projects.chatapp.controller.NotificationController;
import com.zack.projects.chatapp.entity.User;
import com.zack.projects.chatapp.exception.UserNameNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.zack.projects.chatapp.auth.ApplicationUser;
import com.zack.projects.chatapp.dao.ApplicationUserDao;
import com.zack.projects.chatapp.repository.UserRepository;
import static com.zack.projects.chatapp.security.UserRole.*;

@Repository("fake")
@Slf4j
public class ApplicationUserDaoService implements ApplicationUserDao {

	@Autowired
	private final UserRepository userRepository;

	@Autowired
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	private final UserService userService;

	@Autowired
	private final NotificationController notificationController;

	public ApplicationUserDaoService(UserRepository userRepository,
									 PasswordEncoder passwordEncoder,
									 UserService userService,
									 NotificationController notificationController) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.userService = userService;
		this.notificationController = notificationController;
	}

	@Override
	public Optional<ApplicationUser> selectChatappApplicationUserByUserName(String username) {

		log.info(String.format("Find the user with username [%s]", username));
		Optional<ApplicationUser> applicationUser = getApplicationUsers().stream()
				.filter(appUser -> username.equals(appUser.getUsername()))
				.findFirst();

		log.info(String.format("Check if the user with username [%s] exists.", username));
		if(applicationUser.isPresent()) {
			log.info("Retrieve the user object");
			User user = userRepository.findById(applicationUser.get().getUsername()).get();
			log.info("Set the user status to online");
			userService.setUserOnline(user);
			log.info("Save the user object");
			userRepository.save(user);
			try {
				log.info("Notify all users");
				notificationController.updateStatusUsersList(user.getUsername(), true);
			} catch(UserNameNotFoundException e) {
				log.info("Unable to notify users, username [%s] not found", username);
			}

		}
		
		return applicationUser;
	}

	private List<ApplicationUser> getApplicationUsers() {
		
		List<ApplicationUser> applicationUsers = Lists.newArrayList();

		log.info("Retrieve the list of existing users.");
		userRepository.findAll()
					.stream()
					.forEach(user 
							-> {
									applicationUsers.add(new ApplicationUser(
											USER.getGrantedAuthorities(),
											user.getPassword(),
											user.getUsername(),
											user.isAccountNonExpired(),
											user.isAccountNonLocked(),
											user.isCredentialsNonExpired(),
											user.isEnabled()));
							});
		
		return applicationUsers;
							
	}

}
