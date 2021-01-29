package com.zack.projects.chatapp.service;

import com.zack.projects.chatapp.entity.ConversationId;
import com.zack.projects.chatapp.entity.Message;
import com.zack.projects.chatapp.entity.User;
import com.zack.projects.chatapp.entity.UserConversation;
import com.zack.projects.chatapp.repository.ConversationRepository;
import com.zack.projects.chatapp.repository.MessageRepository;
import com.zack.projects.chatapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatappTemplateService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;

    public List<User> getOnlineUsers() {
        return userRepository
                .findAll()
                .stream()
                .filter(user ->
                        user.isOnline())
                .collect(Collectors.toList());
    }

    public List<User> getOfflineUsers() {
        return userRepository
                .findAll()
                .stream()
                .filter(user ->
                        !user.isOnline())
                .collect(Collectors.toList());
    }

    public List<Message> getConversation(String owner, String recipient) {

        User ownerUser = userRepository.findAll()
                .stream()
                .filter(user ->
                        user.getUsername().equals(owner))
                .findFirst()
                .get();

        ConversationId conversationId = new ConversationId(owner, recipient);

        Optional<UserConversation> optionalOwnerRecipientConversation = conversationRepository.findById(conversationId);

        if(!optionalOwnerRecipientConversation.isPresent()) {
            return new ArrayList<Message>();
        }

        List<Message> messages = messageRepository.findAll()
                .stream()
                .filter(message ->
                        message.getSenderRecipient().getSender().equals(owner)
                                && message.getSenderRecipient().getRecipient().equals(recipient) ||
                        message.getSenderRecipient().getSender().equals(recipient)
                                && message.getSenderRecipient().getRecipient().equals(owner))
                .collect(Collectors.toList());

        return messages;

    }
}
