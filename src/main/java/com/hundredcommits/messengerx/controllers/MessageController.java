package com.hundredcommits.messengerx.controllers;

import com.hundredcommits.messengerx.domains.Message;
import com.hundredcommits.messengerx.dtos.NotificationDTO;
import com.hundredcommits.messengerx.service.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

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
        webSocket.convertAndSendToUser(chatMessage.getSenderId(), "/queue/messages", notification);
//        webSocket.convertAndSend("/topic/messages", notification);
    }

    @GetMapping("/chat/index")
    public String index(Model model) {
        // Pobierz listę znajomych z bazy danych lub innego źródła
        List<String> friends = Arrays.asList("Znajomy 1", "Znajomy 2", "Znajomy 3");

        model.addAttribute("friends", friends);
        return "chat/index";
    }
}
