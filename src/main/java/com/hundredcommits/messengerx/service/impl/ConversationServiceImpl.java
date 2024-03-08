package com.hundredcommits.messengerx.service.impl;

import com.hundredcommits.messengerx.domains.Conversation;
import com.hundredcommits.messengerx.repositories.ConversationRepository;
import com.hundredcommits.messengerx.service.ConversationService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;

    public ConversationServiceImpl(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    @Override
    public Optional<String> getConversationId(String senderId, String recipientId, boolean createConversationIfNotExist) {
        return conversationRepository.findConversationByUserOneIdAndUserTwoId(senderId, recipientId)
                .map(Conversation::getChatId)
                .or(() -> {
                    if (createConversationIfNotExist) {
                        return Optional.of(createNewConversationId(senderId, recipientId));
                    }

                    return Optional.empty();
                });
    }

    private String createNewConversationId(String senderId, String recipientId){
        String chatId = String.format("%s_%s",senderId, recipientId);
        Conversation conversation = new Conversation(chatId, senderId, recipientId);
        conversationRepository.save(conversation);
        return chatId;
    }
}
