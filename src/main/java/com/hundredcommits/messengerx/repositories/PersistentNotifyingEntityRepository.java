package com.hundredcommits.messengerx.repositories;

import com.hundredcommits.messengerx.domains.PersistentNotifyingEntity;

import java.util.List;

public interface PersistentNotifyingEntityRepository<T extends PersistentNotifyingEntity> {
    List<T> findAllByNotificationDestinationUserAndCompleted(String username, boolean isCompleted);
}
