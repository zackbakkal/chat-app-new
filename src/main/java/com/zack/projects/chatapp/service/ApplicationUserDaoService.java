package com.zack.projects.chatapp.service;

import java.util.List;
import java.util.Optional;

import com.zack.projects.chatapp.entity.User;
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

	public ApplicationUserDaoService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserService userService) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.userService = userService;
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
			User user = this.userRepository.findById(applicationUser.get().getUsername()).get();
			log.info("Set the user status to online");
			this.userService.setUserOnline(user);
			log.info("Save the user object");
			this.userRepository.save(user);
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
											passwordEncoder.encode(user.getPassword()),
											user.getUsername(),
											user.isAccountNonExpired(),
											user.isAccountNonLocked(),
											user.isCredentialsNonExpired(),
											user.isEnabled()));
							});
		
		return applicationUsers;
							
	}

}
