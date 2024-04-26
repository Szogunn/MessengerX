package com.hundredcommits.messengerx.repositories;

import com.hundredcommits.messengerx.domains.Message;

import java.util.List;
import java.util.Set;

public interface BatchMessageRepository {

    void addMessageToBatch(String messageId);
    void removeMessageFromBatch(String messageId);
    void markMessagesAsRead(List<Message> messages);
    Set<String> getMessagesIdsFromBatch();
}
