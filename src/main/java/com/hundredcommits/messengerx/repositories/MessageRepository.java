package com.hundredcommits.messengerx.repositories;

import com.hundredcommits.messengerx.domains.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {
}
