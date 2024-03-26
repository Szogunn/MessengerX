package com.hundredcommits.messengerx.repositories;

import com.hundredcommits.messengerx.domains.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {

    List<Message> findByConversationId(String conversationId);
}
