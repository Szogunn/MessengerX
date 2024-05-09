package com.hundredcommits.messengerx.service;

import com.hundredcommits.messengerx.domains.Message;
import com.hundredcommits.messengerx.payloads.MessagesPageResponse;
import com.hundredcommits.messengerx.payloads.UnreadMessagesFromFriend;

import java.util.Date;
import java.util.Set;

public interface MessageService {

    Message save(Message message);
    MessagesPageResponse findChatMessages(String senderId, String recipientId, int pageNo, int pageSize);
    void markMessagesAsRead(String messageId, Date date);
    Set<UnreadMessagesFromFriend> countUnreadMessagesPerFriend(String recipientId);
}
