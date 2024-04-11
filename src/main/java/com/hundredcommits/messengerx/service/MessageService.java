package com.hundredcommits.messengerx.service;

import com.hundredcommits.messengerx.domains.Message;
import com.hundredcommits.messengerx.payloads.MessagesPageResponse;

import java.util.List;


public interface MessageService {

    Message save(Message message);
    MessagesPageResponse findChatMessages(String senderId, String recipientId, int pageNo, int pageSize);
    void markMessagesAsRead(List<Message> messages);
}
