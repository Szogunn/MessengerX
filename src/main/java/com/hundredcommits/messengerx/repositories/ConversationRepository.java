package com.hundredcommits.messengerx.repositories;

import com.hundredcommits.messengerx.domains.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface ConversationRepository extends MongoRepository<Conversation, String> {
    @Query(value = "{ $or: [ " +
            "{ $and: [ { userOneId: { $in: [ ?0, ?1 ] } }, { userTwoId: { $in: [ ?0, ?1 ] } } ] }, " +
            "{ $and: [ { userOneId: ?1 }, { userTwoId: ?0 } ] } " +
            "] }")
    Optional<Conversation> findConversationByUserOneIdAndUserTwoId(String senderId, String recipientId);
}
