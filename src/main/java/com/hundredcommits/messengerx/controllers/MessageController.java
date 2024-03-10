package com.hundredcommits.messengerx.controllers;

import com.hundredcommits.messengerx.domains.Message;
import com.hundredcommits.messengerx.dtos.NotificationDTO;
import com.hundredcommits.messengerx.service.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    private final SimpMessagingTemplate webSocket;
    private final MessageService messageService;

    public MessageController(SimpMessagingTemplate webSocket, MessageService messageService) {
        this.webSocket = webSocket;
        this.messageService = messageService;
    }

    @MessageMapping("/chat")
    public void send(@Payload Message chatMessage) {
        Message savedMessage = messageService.save(chatMessage);
        NotificationDTO notification = new NotificationDTO(savedMessage.getId(), savedMessage.getSenderId());
        webSocket.convertAndSendToUser(chatMessage.getRecipientId(), "/queue/messages", notification);
    }
}
