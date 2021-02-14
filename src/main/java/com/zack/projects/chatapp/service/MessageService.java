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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MessageService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    public MessageResponseTemplate sendMessage(Message message)
            throws UserNameNotFoundException {

        if(message.getSenderRecipient().getSender().equals(message.getSenderRecipient().getRecipient())) {
            User user = userRepository.findUserByUsername(message.getSenderRecipient().getSender());

            ConversationId conversationId =
                    new ConversationId(
                            user.getUsername(),
                            user.getUsername());

            Optional<UserConversation> optionalSenderChatappUserConversation =
                    conversationRepository.findById(conversationId);

            if(optionalSenderChatappUserConversation.isPresent()) {
                log.info(String.format("Conversation with id ([%s], [%s]) exists",
                        message.getSenderRecipient().getSender(), message.getSenderRecipient().getRecipient()));
                log.info(String.format("Saving message to the conversation"));
                optionalSenderChatappUserConversation.get().getMessages().add(message);
                log.info(String.format("Saving the conversation"));
                conversationRepository.save(optionalSenderChatappUserConversation.get());

            } else {
                log.info(String.format("Conversation between [%s] and [%s] does not exist",
                        message.getSenderRecipient().getSender(), message.getSenderRecipient().getRecipient()));
                UserConversation conversation = new UserConversation();

                log.info(String.format("Creating a conversation between [%s] and [%s] with conversation id [%s]",
                        message.getSenderRecipient().getSender(), message.getSenderRecipient().getRecipient(), conversationId));
                conversation.setConversationId(conversationId);

                log.info(String.format("Add message to the conversation"));
                conversation.getMessages().add(message);

                log.info(String.format("Add conversation to the user"));
                user.getUserConversations().add(conversation);

                log.info(String.format("Save the user"));
                userRepository.save(user);

            }
            return new MessageResponseTemplate(message);

        }

        log.info(String.format("Retrieving the sender with username [%s] and recipient with username [%s]",
                message.getSenderRecipient().getSender(), message.getSenderRecipient().getRecipient()));
        User senderUser = userRepository.findUserByUsername(message.getSenderRecipient().getSender());
        User receiverUser = userRepository.findUserByUsername(message.getSenderRecipient().getRecipient());

        log.info(String.format("Generating conversation id as ([%s], [%s])",
                message.getSenderRecipient().getSender(), message.getSenderRecipient().getRecipient()));
        ConversationId senderConversationId =
                new ConversationId(
                        message.getSenderRecipient().getSender(),
                        message.getSenderRecipient().getRecipient());

        log.info(String.format("Generating conversation id as ([%s], [%s])",
                message.getSenderRecipient().getSender(), message.getSenderRecipient().getRecipient()));
        ConversationId receiverConversationId =
                new ConversationId(
                        message.getSenderRecipient().getRecipient(),
                        message.getSenderRecipient().getSender());

        log.info(String.format("Checking if a conversation between [%s] and [%s] exists already",
                message.getSenderRecipient().getSender(), message.getSenderRecipient().getRecipient()));
        Optional<UserConversation> optionalSenderChatappUserConversation =
                conversationRepository.findById(senderConversationId);

        Optional<UserConversation> optionalReceiverChatappUserConversation =
                conversationRepository.findById(receiverConversationId);

        ConversationId conversationId =
                new ConversationId(
                        message.getSenderRecipient().getSender(),
                        message.getSenderRecipient().getRecipient()); ;

        if (optionalSenderChatappUserConversation.isPresent()) {

            log.info(String.format("Conversation with id ([%s], [%s]) exists",
                    message.getSenderRecipient().getSender(), message.getSenderRecipient().getRecipient()));
            log.info(String.format("Saving message to the conversation"));
            optionalSenderChatappUserConversation.get().getMessages().add(message);
            log.info(String.format("Saving the conversation"));
            conversationRepository.save(optionalSenderChatappUserConversation.get());

            return new MessageResponseTemplate(message);

        } else if (optionalReceiverChatappUserConversation.isPresent()) {
            System.out.println("found 2");
            log.info(String.format("Conversation with id ([%s], [%s]) exists",
                    message.getSenderRecipient().getRecipient(), message.getSenderRecipient().getSender()));
            log.info(String.format("Saving message to the conversation"));
            optionalReceiverChatappUserConversation.get().getMessages().add(message);
            log.info(String.format("Saving the conversation"));
            conversationRepository.save(optionalReceiverChatappUserConversation.get());

            conversationId.setSender(message.getSenderRecipient().getRecipient());
            conversationId.setRecipient(message.getSenderRecipient().getSender());

            return new MessageResponseTemplate(message);
        }

        log.info(String.format("Conversation between [%s] and [%s] does not exist",
                message.getSenderRecipient().getSender(), message.getSenderRecipient().getRecipient()));
        UserConversation senderUserConversation = new UserConversation();
        UserConversation receiverUserConversation = new UserConversation();

        log.info(String.format("Creating a conversation between [%s] and [%s] with conversation id [%s]",
                message.getSenderRecipient().getSender(), message.getSenderRecipient().getRecipient(), conversationId));
        senderUserConversation.setConversationId(conversationId);
        receiverUserConversation.setConversationId(receiverConversationId);

        log.info(String.format("Add message to the conversation"));
        senderUserConversation.getMessages().add(message);

        log.info(String.format("Add conversation to the user"));
        senderUser.getUserConversations().add(senderUserConversation);
        receiverUser.getUserConversations().add(receiverUserConversation);

        log.info(String.format("Save the user"));
        userRepository.save(senderUser);
        userRepository.save(receiverUser);

        return new MessageResponseTemplate(message);

    }

    public List<Message> getMessages(String owner, String recipient) {

        return messageRepository.findAll()
                .stream()
                .filter(message ->
                        message.getSenderRecipient().getSender().equals(owner)
                                && message.getSenderRecipient().getRecipient().equals(recipient) ||
                                message.getSenderRecipient().getSender().equals(recipient)
                                        && message.getSenderRecipient().getRecipient().equals(owner))
                .collect(Collectors.toList());

    }

}
