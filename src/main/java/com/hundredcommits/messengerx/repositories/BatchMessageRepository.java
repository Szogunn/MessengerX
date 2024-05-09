package com.hundredcommits.messengerx.repositories;

import com.hundredcommits.messengerx.domains.Message;

import java.util.Date;
import java.util.Map;
import java.util.Set;

public interface BatchMessageRepository {

    void addMessageToBatch(String messageId, Date date);
    void removeMessageFromBatch(String messageId);
    void markMessagesAsRead(Map<Message, Date> messages);
    Set<String> getMessagesIdsFromBatch();
}
