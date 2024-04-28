package com.hundredcommits.messengerx.service.impl;

import com.hundredcommits.messengerx.domains.Message;
import com.hundredcommits.messengerx.payloads.MessagesPageResponse;
import com.hundredcommits.messengerx.payloads.UnreadMessagesFromFriend;
import com.hundredcommits.messengerx.repositories.MessageRepository;
import com.hundredcommits.messengerx.repositories.impl.BatchMessageRepositoryImpl;
import com.hundredcommits.messengerx.service.ConversationService;
import com.hundredcommits.messengerx.service.MessageService;
import com.hundredcommits.messengerx.utils.AppUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ConversationService conversationService;
    private final BatchMessageRepositoryImpl batchMessageRepository;

    public MessageServiceImpl(MessageRepository messageRepository, ConversationService conversationService, BatchMessageRepositoryImpl batchMessageRepository) {
        this.messageRepository = messageRepository;
        this.conversationService = conversationService;
        this.batchMessageRepository = batchMessageRepository;
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
        batchMessageRepository.markMessagesAsRead(messages.getContent());
        return new MessagesPageResponse(messages.getContent(), pageable.getPageNumber(), pageable.getPageSize(), messages.getTotalElements(), messages.getTotalPages(), messages.isLast());
    }

    @Override
    public void markMessagesAsRead(String messageId) {
        if (!AppUtil.isEmpty(messageId)) {
            batchMessageRepository.addMessageToBatch(messageId);
        }
    }

    @Override
    public Set<UnreadMessagesFromFriend> countUnreadMessagesPerFriend(String recipientId) {
        if (AppUtil.isEmpty(recipientId)) {
            return Set.of();
        }

        List<Message> unreadMessagesFromDB = messageRepository.findAllByRecipientIdAndCompleted(recipientId, false);
        Set<String> messagesIdsFromBatch = batchMessageRepository.getMessagesIdsFromBatch();
        unreadMessagesFromDB.removeIf(el -> messagesIdsFromBatch.contains(el.getId()));

        Map<String, Long> unreadMessagesFromFriend = unreadMessagesFromDB.stream().collect(Collectors.groupingBy(Message::getSenderId, Collectors.counting()));
        //        unreadMessagesFromDB.stream().collect(Collectors.groupingBy(Message::getSenderId, UnreadMessagesFromFriend::new, Collectors.counting()));
        return unreadMessagesFromFriend.entrySet().stream().map(el -> new UnreadMessagesFromFriend(el.getKey(), el.getValue())).collect(Collectors.toSet());
    }
}
