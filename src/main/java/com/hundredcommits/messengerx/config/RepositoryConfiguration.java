package com.hundredcommits.messengerx.config;

import com.hundredcommits.messengerx.domains.Invitation;
import com.hundredcommits.messengerx.domains.Message;
import com.hundredcommits.messengerx.domains.PersistentNotifyingEntity;
import com.hundredcommits.messengerx.repositories.InvitationRepository;
import com.hundredcommits.messengerx.repositories.MessageRepository;
import com.hundredcommits.messengerx.repositories.PersistentNotifyingEntityRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RepositoryConfiguration {

    @Bean
    public Map<Class<? extends PersistentNotifyingEntity>, PersistentNotifyingEntityRepository<?>> repositoryMap (
            InvitationRepository invitationRepository,
            MessageRepository messageRepository
    ) {
        Map<Class<? extends PersistentNotifyingEntity>, PersistentNotifyingEntityRepository<?>> map = new HashMap<>();
        map.put(Invitation.class, invitationRepository);
        map.put(Message.class, messageRepository);
        // Do dodania pozostałe repozytoria, jeśli istnieją
        return map;
    }
}
