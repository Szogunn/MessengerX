package com.hundredcommits.messengerx.service;

import java.util.Optional;

public interface ConversationService {

    Optional<String> getConversationId(String senderId, String recipientId, boolean createConversationIfNotExist);
}
