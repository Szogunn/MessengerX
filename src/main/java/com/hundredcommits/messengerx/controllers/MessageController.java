package com.hundredcommits.messengerx.controllers;

import com.hundredcommits.messengerx.domains.Message;
import com.hundredcommits.messengerx.dtos.NotificationDTO;
import com.hundredcommits.messengerx.jwt.UserDetailsImpl;
import com.hundredcommits.messengerx.service.MessageService;
import com.hundredcommits.messengerx.utils.AppUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.Objects;

@Controller
@Slf4j
public class MessageController {

    private final SimpMessagingTemplate webSocket;
    private final MessageService messageService;

    public MessageController(SimpMessagingTemplate webSocket, MessageService messageService) {
        this.webSocket = webSocket;
        this.messageService = messageService;
    }

    @MessageMapping("/chat")
    public void send(@Payload Message chatMessage, SimpMessageHeaderAccessor header) {
        if (chatMessage == null){
            return;
        }

        if (AppUtil.isEmpty(chatMessage.getRecipientId())) {
            log.warn("Recipient is empty. Cannot process that message");
            return;
        }

        try {
            UserDetailsImpl user = (UserDetailsImpl) ((UsernamePasswordAuthenticationToken) header.getHeader("simpUser")).getPrincipal();
            String senderUsername = user.getUsername();

            if (!Objects.equals(senderUsername, chatMessage.getSenderId())){
                return;
            }
        } catch (Exception ex){
            log.error("Occurs problem under message processing" , ex);
            return;
        }

        Message savedMessage = messageService.save(chatMessage);
        NotificationDTO notification = new NotificationDTO(savedMessage.getContent(), savedMessage.getSenderId());
        webSocket.convertAndSendToUser(chatMessage.getRecipientId(), "/queue/messages", notification);
//        webSocket.convertAndSend("/topic/messages", notification);
    }
}
