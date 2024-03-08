package com.hundredcommits.messengerx.controllers;

import com.hundredcommits.messengerx.domains.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    private final SimpMessagingTemplate webSocket;

    public MessageController(SimpMessagingTemplate webSocket) {
        this.webSocket = webSocket;
    }

    @MessageMapping("/chat")
    public void send(@Payload Message chatMessage) {
        Message message = new Message(chatMessage.getConversationId(), chatMessage.getSenderId(), chatMessage.getContent());
        webSocket.convertAndSendToUser(chatMessage.getRecipientId(), "/queue/messages", message);
    }
}
