package com.hundredcommits.messengerx.controllers;

import com.hundredcommits.messengerx.domains.Message;
import com.hundredcommits.messengerx.dtos.MessageDTO;
import com.hundredcommits.messengerx.jwt.UserDetailsImpl;
import com.hundredcommits.messengerx.payloads.MessagesPageResponse;
import com.hundredcommits.messengerx.service.MessageService;
import com.hundredcommits.messengerx.utils.AppUtil;
import com.hundredcommits.messengerx.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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
        MessageDTO message = new MessageDTO(savedMessage.getId(), savedMessage.getContent(), savedMessage.getSenderId());
        webSocket.convertAndSendToUser(chatMessage.getRecipientId(), "/queue/messages", message);
    }

    @GetMapping("/messages/{recipientId}")
    public ResponseEntity<MessagesPageResponse> findChatMessages(@PathVariable String recipientId, @RequestParam int pageNo, @RequestParam int pageSize){
        String senderId = SecurityUtils.getAuthenticatedUsername();
        MessagesPageResponse messages = messageService.findChatMessages(senderId, recipientId, pageNo, pageSize);

        return ResponseEntity.ok(messages);
    }

    @MessageMapping("/readMessages")
    public void handleReadMessages(@Payload String readMessageIds, SimpMessageHeaderAccessor headerAccessor) {
        messageService.markMessagesAsRead(readMessageIds);
    }
}
