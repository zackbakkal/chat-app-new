package com.zack.projects.chatapp.VO;

import com.zack.projects.chatapp.entity.SenderRecipient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MissedMessageResponseTemplate {

    private SenderRecipient senderRecipient;

}
