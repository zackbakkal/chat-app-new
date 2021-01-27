package com.zack.projects.chatapp.entity;

import com.zack.projects.chatapp.repository.ConversationRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "conversations")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserConversation {

    @EmbeddedId
    private ConversationId conversationId;

    @OneToMany(cascade = CascadeType.ALL)
    private Collection<Message> messages = new ArrayList<>();

}
