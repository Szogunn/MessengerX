package com.hundredcommits.messengerx.repositories;

import com.hundredcommits.messengerx.domains.Invitation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface InvitationRepository extends MongoRepository<Invitation, String>, PersistentNotifyingEntityRepository<Invitation> {

    Optional<Invitation> findByFromUserAndToUserAndCompleted(String fromUser, String toUser, boolean isCompleted);
}
