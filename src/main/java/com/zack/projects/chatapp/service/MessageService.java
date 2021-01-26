package com.zack.projects.chatapp.service;

import com.zack.projects.chatapp.VO.MessageResponseTemplate;
import com.zack.projects.chatapp.entity.User;
import com.zack.projects.chatapp.entity.UserConversation;
import com.zack.projects.chatapp.entity.ConversationId;
import com.zack.projects.chatapp.entity.Message;
import com.zack.projects.chatapp.exception.UserNameNotFoundException;
import com.zack.projects.chatapp.repository.UserRepository;
import com.zack.projects.chatapp.repository.ConversationRepository;
import com.zack.projects.chatapp.repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
@Slf4j
public class MessageService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    public MessageResponseTemplate sendMessage(Message message) throws UserNameNotFoundException {

        log.info(String.format("Retrieving the message sender with username [%s]",
                message.getSenderRecipient().getSender()));
        User user = userRepository.findUserByUsername(message.getSenderRecipient().getSender());

        log.info(String.format("Setting message sent date"));
        message.setDateSent(new Timestamp(System.currentTimeMillis()));

        log.info(String.format("Generating conversation id as ([%s], [%s])",
                message.getSenderRecipient().getSender(), message.getSenderRecipient().getRecipient()));
        ConversationId conversationId =
                new ConversationId(
                        message.getSenderRecipient().getSender(),
                        message.getSenderRecipient().getRecipient());

        log.info(String.format("Checking if a conversation between [%s] and [%s] exists already",
                message.getSenderRecipient().getSender(), message.getSenderRecipient().getRecipient()));
        Optional<UserConversation> optionalChatappUserConversation =
                conversationRepository.findById(conversationId);

        if (optionalChatappUserConversation.isPresent()) {
            log.info(String.format("Conversation with id ([%s], [%s]) exists",
                    message.getSenderRecipient().getSender(), message.getSenderRecipient().getRecipient()));
            log.info(String.format("Saving message to the conversation"));
            optionalChatappUserConversation.get().getMessages().add(message);
            log.info(String.format("Saving the conversation"));
            conversationRepository.save(optionalChatappUserConversation.get());

            return new MessageResponseTemplate(message);
        }

        log.info(String.format("Conversation between [%s] and [%s] does not exist",
                message.getSenderRecipient().getSender(), message.getSenderRecipient().getRecipient()));
        UserConversation conversation = new UserConversation();

        log.info(String.format("Creating a conversation between [%s] and [%s] with conversation id [%s]",
                message.getSenderRecipient().getSender(), message.getSenderRecipient().getRecipient(), conversationId));
        conversation.setConversationId(conversationId);

        log.info(String.format("Add message to the conversation"));
        conversation.getMessages().add(message);

        log.info(String.format("Add conversation to the user"));
        user.getConversations().add(conversation);

        log.info(String.format("Save the user"));
        userRepository.save(user);

        return new MessageResponseTemplate(message);

    }


}
