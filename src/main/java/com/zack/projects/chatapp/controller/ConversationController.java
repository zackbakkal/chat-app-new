package com.zack.projects.chatapp.controller;

import com.zack.projects.chatapp.VO.MessageResponseTemplate;
import com.zack.projects.chatapp.entity.ConversationId;
import com.zack.projects.chatapp.entity.Message;
import com.zack.projects.chatapp.entity.UserConversation;
import com.zack.projects.chatapp.repository.ConversationRepository;
import com.zack.projects.chatapp.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/conversations")
@Slf4j
public class ConversationController {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageService messageService;

    @Autowired
    private NotificationController notificationController;

    @GetMapping(value = "{recipient}")
    public List<MessageResponseTemplate> getConversation(
            @PathVariable String recipient,
            HttpServletRequest httpServletRequest) {

        String owner = httpServletRequest.getRemoteUser();

        return getConversation(owner, recipient);

    }

    private List<MessageResponseTemplate> getConversation(String owner, String recipient) {

        ConversationId conversationId = new ConversationId(owner, recipient);

        Optional<UserConversation> optionalOwnerRecipientConversation = conversationRepository.findById(conversationId);

        if(!optionalOwnerRecipientConversation.isPresent()) {
            return new ArrayList<MessageResponseTemplate>();
        }

        List<Message> messages = messageService.getMessages(owner, recipient);

        List<MessageResponseTemplate> messageResponseTemplates = new ArrayList<>();

        messages.stream()
                .forEach(message ->
                        messageResponseTemplates.add(new MessageResponseTemplate(message)));

        notificationController.notificationReceived(recipient, owner);

        return messageResponseTemplates;

    }

}
