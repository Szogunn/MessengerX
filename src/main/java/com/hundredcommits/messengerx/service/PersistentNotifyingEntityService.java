package com.hundredcommits.messengerx.service;

import com.hundredcommits.messengerx.domains.PersistentNotifyingEntity;

import java.util.List;

public interface PersistentNotifyingEntityService {

    List<PersistentNotifyingEntity> getAllPersistenceNotifications(String username, boolean isCompleted);
}
