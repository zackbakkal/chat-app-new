package com.zack.projects.chatapp.entity;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class SenderRecipient implements Serializable {

    private String sender;
    private String recipient;

}
