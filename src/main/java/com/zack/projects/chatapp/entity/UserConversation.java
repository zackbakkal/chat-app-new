package com.zack.projects.chatapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "conversations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserConversation {

    @EmbeddedId
    private ConversationId conversationId;

    @OneToMany(cascade = CascadeType.ALL)
    private Collection<Message> messages = new ArrayList<>();

}
