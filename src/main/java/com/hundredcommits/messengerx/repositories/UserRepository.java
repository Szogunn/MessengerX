package com.hundredcommits.messengerx.repositories;

import com.hundredcommits.messengerx.domains.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Set<User> findAllByUsernameIn(Collection<String> usernames);
}
