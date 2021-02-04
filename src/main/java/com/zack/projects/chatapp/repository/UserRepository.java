package com.zack.projects.chatapp.repository;

import com.zack.projects.chatapp.entity.User;
import com.zack.projects.chatapp.exception.UserNameNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findUserByUsername(String username) throws UserNameNotFoundException;

}
