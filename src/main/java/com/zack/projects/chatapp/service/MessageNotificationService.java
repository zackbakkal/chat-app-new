package com.zack.projects.chatapp.service;

import com.zack.projects.chatapp.VO.MessageResponseTemplate;
import com.zack.projects.chatapp.VO.MissedMessageResponseTemplate;
import com.zack.projects.chatapp.VO.UserOnlineStatusResponseTemplate;
import com.zack.projects.chatapp.entity.Message;
import com.zack.projects.chatapp.entity.SenderRecipient;
import com.zack.projects.chatapp.exception.UserNameNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MessageNotificationService {

    private Map<String, SseEmitter> sseEmitters = new HashMap<>();

    public static List<SenderRecipient> missedEmitters = new CopyOnWriteArrayList<>();

    @Autowired
    private UserService userService;

    public SseEmitter subscribeUser(String username) {

        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

        sendInitEvent(sseEmitter);

        sseEmitters.put(username, sseEmitter);

        sseEmitter.onTimeout(() -> sseEmitters.remove(sseEmitter));
        sseEmitter.onError((e) -> sseEmitters.remove(sseEmitter));

        log.info(String.format("Sending notifications to [%s]", username));
        deliverMissedMessages(username);

        return sseEmitter;
    }

    private void sendInitEvent(SseEmitter sseEmitter) {
        try {
            sseEmitter.send(SseEmitter.event().name("INIT"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deliverMessage(Message message) throws UserNameNotFoundException {

        SseEmitter sseEmitter = sseEmitters.get(message.getSenderRecipient().getRecipient());

        String username = message.getSenderRecipient().getRecipient();

        UserOnlineStatusResponseTemplate userOnlineStatusResponseTemplate = userService.getUserStatus(username);
        boolean online = userOnlineStatusResponseTemplate.isOnline();

        if(sseEmitter != null && online) {
            MessageResponseTemplate sentMessage = new MessageResponseTemplate(message);

            try {
                sseEmitter.send(SseEmitter.event().name("newMessage").data(sentMessage));
                return;
            } catch (IOException e) {
                sseEmitters.remove(sseEmitter);
                return;
            }
        }

        SenderRecipient senderRecipient =
                new SenderRecipient(
                        message.getSenderRecipient().getSender(),
                        message.getSenderRecipient().getRecipient());

        missedEmitters.add(senderRecipient);

    }

    public void deliverMissedMessages(String recipient) {

        log.info(String.format("Retrieving list of missed Emitters"));
        List<SenderRecipient> senderRecipients = missedEmitters.stream()
                .filter(senderRecipient ->
                        senderRecipient.getRecipient().equals(recipient))
                .collect(Collectors.toList());

        if(!senderRecipients.isEmpty()) {

            log.info(String.format("Retrieving the emitter for [%s]", recipient));
            SseEmitter sseEmitter = sseEmitters.get(recipient);
            log.info(String.format("Removing the emitter from the missed emitter's list"));


            log.info(String.format("Sending notifications to [%s] from [%s] users", recipient, senderRecipients.size()));
            senderRecipients.forEach(senderRecipient -> {

                MissedMessageResponseTemplate sentMessage = new MissedMessageResponseTemplate(senderRecipient);

                try {
                    sseEmitter.send(SseEmitter.event().name("newMessage").data(sentMessage));
                    log.info(String.format("Notifications from [%s] sent successful", senderRecipient.getSender()));
                    //missedEmitters.remove(senderRecipient);
                } catch (IOException e) {
                    log.info(String.format("Notifications from [%s] failed", senderRecipient.getSender()));
                    sseEmitters.remove(sseEmitter);
                }
            });

        }

    }

    public void notificationReceived(String sender, String recipient) {

        SenderRecipient senderRecipient = new SenderRecipient(sender, recipient);

        log.info(String.format("removing notifications from [%s]", sender));
        missedEmitters.removeAll(Collections.singleton(senderRecipient));

    }

    public void updateUsersList(String username, boolean status) {

        Collection<SseEmitter> sseEmittersValues = sseEmitters.values();

        UserOnlineStatusResponseTemplate userOnlineStatusResponseTemplate =
                new UserOnlineStatusResponseTemplate(username, status);

        sseEmittersValues
                .forEach(sseEmitter ->
                {
                    try {
                        sseEmitter
                                .send(SseEmitter
                                        .event()
                                        .name("updateUsersList").data(userOnlineStatusResponseTemplate));
                    } catch (IOException e) {
                        sseEmitters.remove(sseEmitter);
                    }
                });

    }
}
