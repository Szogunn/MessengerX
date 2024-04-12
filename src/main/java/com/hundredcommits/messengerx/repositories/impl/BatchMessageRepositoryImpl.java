package com.hundredcommits.messengerx.repositories.impl;

import com.hundredcommits.messengerx.domains.Message;
import com.hundredcommits.messengerx.repositories.BatchMessageRepository;
import com.hundredcommits.messengerx.repositories.MessageRepository;
import com.hundredcommits.messengerx.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class BatchMessageRepositoryImpl implements BatchMessageRepository {

    private final Set<String> messagesIdBatch = new CopyOnWriteArraySet<>();
    private final MessageRepository messageRepository;
    private final ScheduledExecutorService scheduler;

    public BatchMessageRepositoryImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.scheduler.scheduleAtFixedRate(this::markMessagesInBatchAsRead, 0, 5, TimeUnit.MINUTES);
    }

    @Override
    public void addMessageToBatch(String messageId) {
        messagesIdBatch.add(messageId);
    }

    @Override
    public void removeMessageFromBatch(String messageId) {
        messagesIdBatch.remove(messageId);
    }

    @Override
    public void markMessagesAsRead(List<Message> messages) {
        String authRecipientUser = SecurityUtils.getAuthenticatedUsername();
        List<Message> readMessages = messages.stream()
                .filter(message -> !message.getCompleted())
                .filter(message -> message.getRecipientId().equals(authRecipientUser))
                .peek(Message::setCompleted)
                .toList();

        if (!readMessages.isEmpty()) {
            messageRepository.saveAll(readMessages);
            readMessages.stream()
                    .map(Message::getId)
                    .forEach(messagesIdBatch::remove);
            log.info("Updated {} messages in the database", messages.size());
        }
    }

    private void markMessagesInBatchAsRead() {
        if (messagesIdBatch.isEmpty()) {
            return;
        }

        List<Message> messagesFromBatch = messageRepository.findAllById(messagesIdBatch);
        if (!messagesFromBatch.isEmpty()) {
            markMessagesAsRead(messagesFromBatch);
            this.messagesIdBatch.clear();
        }
    }
}
