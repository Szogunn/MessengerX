package com.hundredcommits.messengerx.service.impl;

import com.hundredcommits.messengerx.domains.PersistentNotifyingEntity;
import com.hundredcommits.messengerx.factories.PersistentNotifyingEntityRepositoryFactoryImpl;
import com.hundredcommits.messengerx.service.PersistentNotifyingEntityService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersistentNotifyingEntityServiceImpl implements PersistentNotifyingEntityService {

    private final PersistentNotifyingEntityRepositoryFactoryImpl pnerFactory;

    public PersistentNotifyingEntityServiceImpl(PersistentNotifyingEntityRepositoryFactoryImpl pnerFactory) {
        this.pnerFactory = pnerFactory;
    }

    @Override
    public List<PersistentNotifyingEntity> getAllPersistenceNotifications(String username, boolean isCompleted) {
        List<PersistentNotifyingEntity> notifications = new ArrayList<>();
        pnerFactory.getAllRepositories().forEach(el -> notifications.addAll(el.findAllByNotificationDestinationUserAndCompleted(username, isCompleted)));

        return notifications;
    }

}
