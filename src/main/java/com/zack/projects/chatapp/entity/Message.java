package com.zack.projects.chatapp.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "messages")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long messageId;

    private SenderRecipient senderRecipient;
    private String text;
    private Timestamp dateSent;

    @MapsId("conversationId")
    @JoinColumns({
            @JoinColumn(name = "sender", referencedColumnName = "sender"),
            @JoinColumn(name = "recipient", referencedColumnName = "recipient"),
    })
    @ManyToOne
    private UserConversation userConversation;

}
