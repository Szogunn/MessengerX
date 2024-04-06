package com.hundredcommits.messengerx.factories;

import com.hundredcommits.messengerx.domains.PersistentNotifyingEntity;
import com.hundredcommits.messengerx.repositories.PersistentNotifyingEntityRepository;

public interface PersistentNotifyingEntityRepositoryFactory {
    <T extends PersistentNotifyingEntity> PersistentNotifyingEntityRepository<T> getRepositoryForEntity(Class<T> entityType);
}