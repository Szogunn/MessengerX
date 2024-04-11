package com.hundredcommits.messengerx.service.impl;

import com.hundredcommits.messengerx.domains.Message;
import com.hundredcommits.messengerx.payloads.MessagesPageResponse;
import com.hundredcommits.messengerx.repositories.MessageRepository;
import com.hundredcommits.messengerx.service.ConversationService;
import com.hundredcommits.messengerx.service.MessageService;
import com.hundredcommits.messengerx.utils.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public MessagesPageResponse findChatMessages(String senderId, String recipientId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.Direction.DESC, "timestamp");
        var conversationId = conversationService.getConversationId(senderId, recipientId, false);
        if (conversationId.isEmpty()){
            return MessagesPageResponse.EMPTY_RESPONSE;
        }

        Page<Message> messages = messageRepository.findByConversationId(conversationId.get(), pageable);
        markMessagesAsRead(messages.getContent());
        return new MessagesPageResponse(messages.getContent(), pageable.getPageNumber(), pageable.getPageSize(), messages.getTotalElements(), messages.getTotalPages(), messages.isLast());
    }

    @Override
    public void markMessagesAsRead(List<Message> messages) {
        String authRecipientUser = SecurityUtils.getAuthenticatedUsername();
        List<Message> markedMessages = messages.stream()
                .filter(message -> !message.getCompleted())
                .filter(message -> message.getRecipientId().equals(authRecipientUser))
                .peek(Message::setCompleted)
                .toList();
        messageRepository.saveAll(markedMessages);
    }
}
