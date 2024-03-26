package com.hundredcommits.messengerx.service;

import com.hundredcommits.messengerx.domains.Message;

import java.util.List;

public interface MessageService {

    Message save(Message message);
    List<Message> findChatMessages(String senderId, String recipientId);
}
