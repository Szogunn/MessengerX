package com.hundredcommits.messengerx.repositories.impl;

import com.hundredcommits.messengerx.domains.Message;
import com.hundredcommits.messengerx.repositories.BatchMessageRepository;
import com.hundredcommits.messengerx.repositories.MessageRepository;
import com.hundredcommits.messengerx.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class BatchMessageRepositoryImpl implements BatchMessageRepository {

    private final Map<String, Date> messagesIdBatch = new ConcurrentHashMap<>();
    private final MessageRepository messageRepository;
    private final ScheduledExecutorService scheduler;

    public BatchMessageRepositoryImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.scheduler.scheduleAtFixedRate(this::markMessagesInBatchAsRead, 0, 5, TimeUnit.MINUTES);
    }

    @Override
    public void addMessageToBatch(String messageId, Date date) {
        messagesIdBatch.put(messageId, date);
    }

    @Override
    public void removeMessageFromBatch(String messageId) {
        messagesIdBatch.remove(messageId);
    }

    @Override
    public void markMessagesAsRead(Map<Message, Date> messages) {
        String authRecipientUser = SecurityUtils.getAuthenticatedUsername();
        List<Message> readMessages = messages.entrySet()
                .stream()
                .filter(message -> !message.getKey().getCompleted())
                .filter(message -> message.getKey().getRecipientId().equals(authRecipientUser))
                .peek(message -> message.getKey().setCompleted(message.getValue()))
                .map(Map.Entry::getKey)
                .toList();

        if (!readMessages.isEmpty()) {
            messageRepository.saveAll(readMessages);
            readMessages.stream()
                    .map(Message::getId)
                    .forEach(messagesIdBatch::remove);
            log.info("Updated {} messages in the database", readMessages.size());
        }
    }

    @Override
    public Set<String> getMessagesIdsFromBatch() {
        return messagesIdBatch.keySet();
    }

    private void markMessagesInBatchAsRead() {
        if (messagesIdBatch.isEmpty()) {
            return;
        }

        List<Message> messagesFromBatch = messageRepository.findAllById(messagesIdBatch.keySet());
        if (!messagesFromBatch.isEmpty()) {
            Map<Message, Date> messageDateMap = messagesFromBatch.stream()
                    .collect(Collectors.toMap(Function.identity(), v -> messagesIdBatch.get(v.getId())));
            markMessagesAsRead(messageDateMap);
            this.messagesIdBatch.clear();
        }
    }
}
