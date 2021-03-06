package com.zack.projects.chatapp.entity;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationId implements Serializable {

    private String sender;
    private String recipient;

}
