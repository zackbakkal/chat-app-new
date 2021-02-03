package com.zack.projects.chatapp.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String username;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean isOnline;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "conversations", referencedColumnName = "username")
    private List<UserConversation> userConversations = new CopyOnWriteArrayList<>();

}
