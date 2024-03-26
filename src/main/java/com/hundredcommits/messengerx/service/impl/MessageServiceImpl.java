package com.hundredcommits.messengerx.service.impl;

import com.hundredcommits.messengerx.domains.Message;
import com.hundredcommits.messengerx.repositories.MessageRepository;
import com.hundredcommits.messengerx.service.ConversationService;
import com.hundredcommits.messengerx.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ConversationService conversationService;

    public MessageServiceImpl(MessageRepository messageRepository, ConversationService conversationService) {
        this.messageRepository = messageRepository;
        this.conversationService = conversationService;
    }

    @Override
    public Message save(Message message) {
        if (message == null){
            return null;
        }

        String conversationId = conversationService.getConversationId(message.getSenderId(), message.getRecipientId(), true)
                .orElseThrow();
        message.setConversationId(conversationId);
        return messageRepository.save(message);
    }

    @Override
    public List<Message> findChatMessages(String senderId, String recipientId) {
        var conversationId = conversationService.getConversationId(senderId, recipientId, false);
        return conversationId.map(messageRepository::findByConversationId).orElse(List.of());
    }
}
