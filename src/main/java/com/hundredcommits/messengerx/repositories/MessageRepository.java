package com.hundredcommits.messengerx.repositories;

import com.hundredcommits.messengerx.domains.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String>, PersistentNotifyingEntityRepository<Message> {

    Page<Message> findByConversationId(String conversationId, Pageable pageable);
}
