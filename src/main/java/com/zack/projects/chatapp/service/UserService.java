package com.zack.projects.chatapp.service;

import com.amazonaws.services.xray.model.Http;
import com.zack.projects.chatapp.VO.ProfileImageResponseTemplate;
import com.zack.projects.chatapp.VO.ProfileResponseTemplate;
import com.zack.projects.chatapp.VO.UserOnlineStatusResponseTemplate;
import com.zack.projects.chatapp.VO.UserAvailabilityResponseTemplate;
import com.zack.projects.chatapp.amazon.bucket.BucketName;
import com.zack.projects.chatapp.amazon.filestore.service.ImageStoreService;
import com.zack.projects.chatapp.controller.NotificationController;
import com.zack.projects.chatapp.entity.User;
import com.zack.projects.chatapp.exception.UserNameExistsException;
import com.zack.projects.chatapp.exception.UserNameNotFoundException;
import com.zack.projects.chatapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final long MAX_FILE_SIZE = 5000000;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private NotificationController notificationController;

    @Autowired
    private ImageStoreService imageStoreService;

    public ProfileResponseTemplate saveUser(User user) throws UserNameExistsException {
        log.info(String.format("Checking if username [%s] already exists", user.getUsername()));
        boolean userNameExists = userRepository.existsById(user.getUsername());

        if(userNameExists) {
            log.info(String.format("username [%s] is taken", user.getUsername()));
            throw new UserNameExistsException(String.format("Username [%s] is taken", user.getUsername()));
        }

        log.info(String.format("username [%s] is available: ", user.getUsername()));

        log.info("Encoding password");
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        log.info("Activating user account");
        activateUserAccount(user);

        log.info("Setting user availability to available");
        activateUserAccount(user);

        log.info("Setting user availability");
        initializeUserAvailability(user);

        log.info(String.format("Adding user: [%s]", user));
        userRepository.save(user);

        log.info(String.format("Creating response"));
        ProfileResponseTemplate profileResponseTemplate = new ProfileResponseTemplate(user);

        return profileResponseTemplate;
    }

    public ProfileResponseTemplate getUserProfile(String username) throws UserNameNotFoundException {

        User user = findUserByUsername(username);

        log.info(String.format("Generating [%s] profile", username));
        return new ProfileResponseTemplate(user);
    }

    public List<ProfileResponseTemplate> getUsersProfiles() {

        List<ProfileResponseTemplate> usersProfiles = new ArrayList<>();

        log.info(String.format("Retrieving users profiles"));
        userRepository.findAll()
                .stream()
                .forEach(user ->
                        usersProfiles.add(new ProfileResponseTemplate(user)));

        return usersProfiles;
    }

    public UserOnlineStatusResponseTemplate setUserOnline(String username) throws UserNameNotFoundException {

        User user = findUserByUsername(username);

        log.info(String.format("Setting user [%s] online", username));
        user.setOnline(true);

        log.info(String.format("Generating [%s] online status", username));
        UserOnlineStatusResponseTemplate userOnlineStatusResponseTemplate =
                new UserOnlineStatusResponseTemplate(user);

        return userOnlineStatusResponseTemplate;
    }

    // Set user online
    public void setUserOnline(User user) {
        user.setOnline(true);;
    }

    public UserOnlineStatusResponseTemplate setUserOffline(String username) throws UserNameNotFoundException {

        User user = findUserByUsername(username);

        log.info(String.format("Setting user [%s] online", username));
        user.setOnline(false);

        log.info(String.format("Generating [%s] online status", username));
        UserOnlineStatusResponseTemplate userOnlineStatusResponseTemplate =
                new UserOnlineStatusResponseTemplate(user);

        log.info(String.format("Saving user"));
        userRepository.save(user);

        return userOnlineStatusResponseTemplate;
    }

    // Set user offline
    public void setUserOffline(User user) {
        user.setOnline(false);;
    }

    public UserOnlineStatusResponseTemplate getUserOnlineStatus(String username) throws UserNameNotFoundException {

        User user = findUserByUsername(username);

        log.info(String.format("Generating [%s] online status", username));
        UserOnlineStatusResponseTemplate userOnlineStatusResponseTemplate =
                new UserOnlineStatusResponseTemplate(user);

        return userOnlineStatusResponseTemplate;
    }

    private void activateUserAccount(User user) {
        log.info(String.format("Activating User [%s] account", user));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
    }

    public void initializeUserAvailability(User user) {
        user.setAvailability("available");
    }

    public User findUserByUsername(String username) throws UserNameNotFoundException {
        log.info(String.format("Looking for username: [%s]", username));
        return userRepository.findById(username)
                .orElseThrow(() -> {
                    log.info(String.format("Username [%s] does not exist", username));
                    return new UserNameNotFoundException(String.format("Username [%s] not found", username));
                });

    }

    public List<UserOnlineStatusResponseTemplate> getOnlineUsers() {

        List<UserOnlineStatusResponseTemplate> userOnlineStatusResponseTemplates = new ArrayList<>();

        log.info((String.format("Retrieving user with online status")));
        userRepository
                .findAll()
                .stream()
                .filter(user ->
                        user.isOnline())
                .collect(Collectors.toList())
                .forEach(user ->
                        userOnlineStatusResponseTemplates.add(new UserOnlineStatusResponseTemplate(user)));

        return userOnlineStatusResponseTemplates;

    }

    public List<UserOnlineStatusResponseTemplate> getOfflineUsers() {

        List<UserOnlineStatusResponseTemplate> userOnlineStatusResponseTemplates = new ArrayList<>();

        log.info((String.format("Retrieving user with offline status")));
        userRepository
                .findAll()
                .stream()
                .filter(user ->
                        !user.isOnline())
                .collect(Collectors.toList())
                .forEach(user ->
                        userOnlineStatusResponseTemplates.add(new UserOnlineStatusResponseTemplate(user)));

        return userOnlineStatusResponseTemplates;
    }

    public List<UserOnlineStatusResponseTemplate> getUsersStatus() {

        List<UserOnlineStatusResponseTemplate> userOnlineStatusResponseTemplates = new ArrayList<>();

        log.info((String.format("Retrieving users' statuses")));
        userRepository
                .findAll()
                .stream()
                .forEach(user ->
                        userOnlineStatusResponseTemplates.add(new UserOnlineStatusResponseTemplate(user)));

        return userOnlineStatusResponseTemplates;

    }

    public UserOnlineStatusResponseTemplate searchUser(String username) throws UserNameNotFoundException {

        log.info((String.format("Searching username [%s]", username)));
        User user = userRepository.findUserByUsername(username);
        return new UserOnlineStatusResponseTemplate(user);

    }

    public List<UserOnlineStatusResponseTemplate> searchUsersStartWith(String startWith) {

        List<UserOnlineStatusResponseTemplate> userOnlineStatusResponseTemplates = new ArrayList<>();

        log.info((String.format("Searching usernames that start with [%s]", startWith)));
        userRepository.findAll()
                .stream()
                .filter(user ->
                        user.getUsername().startsWith(startWith))
                .collect(Collectors.toList())
                .forEach(user->
                        userOnlineStatusResponseTemplates.add(new UserOnlineStatusResponseTemplate(user)));

        return userOnlineStatusResponseTemplates;

    }

    public UserAvailabilityResponseTemplate getUserAvailability(String username) throws UserNameNotFoundException {

        log.info((String.format("Retrieving user with username [%s]", username)));
        User user = userRepository.findById(username).get();
        return new UserAvailabilityResponseTemplate(user);

    }

    public UserAvailabilityResponseTemplate setUserAvailability(String username, String availability) throws UserNameNotFoundException {

        log.info((String.format("Retrieving user with username [%s]", username)));
        User user = userRepository.findById(username).get();
        log.info((String.format("Setting user's availability to [%s]", availability)));
        user.setAvailability(availability);
        log.info("Saving user");
        userRepository.save(user);
        log.info(String.format("Notify Users that user [%s] has changed his status to [%s]", username, availability));
        notificationController.updateAvailabilityUsersList(username, availability);

        return new UserAvailabilityResponseTemplate(user);

    }

    public void updateProfile(ProfileResponseTemplate updatedProfile) throws UserNameNotFoundException {

        User user = userRepository.findUserByUsername(updatedProfile.getUsername());
        ProfileResponseTemplate userProfile = getUserProfile(updatedProfile.getUsername());

        log.info(String.format("Updating [%s]'s profile", user.getUsername()));
        if(!userProfile.equals(updatedProfile)) {
            user.setFirstName(updatedProfile.getFirstName());
            user.setLastName(updatedProfile.getLastName());
            user.setEmail(updatedProfile.getEmail());
            userRepository.save(user);
        }
    }

    public ProfileImageResponseTemplate updateProfileImage(String username, MultipartFile file) throws UserNameNotFoundException {

        log.info("Updating profile image");

        log.info(String.format("Retrieving user [%s]", username));
        User user = userRepository.findUserByUsername(username);

        log.info(String.format("Checking if uploaded file is not empty"));
        if(!file.isEmpty() && file.getSize() < MAX_FILE_SIZE) {
            String profileImageName = imageStoreService.updateImage(user, file);
            log.info(String.format("Updating user [%s]", username));
            user.setProfileImageName(profileImageName);
            log.info(String.format("Saving user"));
            userRepository.save(user);

            String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), username);

            String profileImageUrl = ImageStoreService.buildProfileImageUrl(username, profileImageName);
            log.info(String.format("Profile image uploaded at [%s]", profileImageUrl));
            ProfileImageResponseTemplate profileImageResponseTemplate =
                    new ProfileImageResponseTemplate(username, profileImageUrl);

            notificationController.updateUserAvatar(username);

            return profileImageResponseTemplate;

        } else {
            log.info(String.format("Cannot upload file with size [%s] Bytes", file.getSize()));
            throw new IllegalStateException(String.format("Cannot upload file with size [%s] Bytes", file.getSize()));
        }
    }

    public byte[] getProfileImage(String username) throws UserNameNotFoundException {
        User user = userRepository.findById(username).get();
        log.info(String.format("Downloading image for user [%s]", username));
        return imageStoreService.downloadProfileImage(user);

    }

}
