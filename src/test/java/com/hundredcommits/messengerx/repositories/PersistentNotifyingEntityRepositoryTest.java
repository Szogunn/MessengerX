package com.hundredcommits.messengerx.repositories;

import com.hundredcommits.messengerx.domains.Invitation;
import com.hundredcommits.messengerx.domains.PersistentNotifyingEntity;
import com.hundredcommits.messengerx.service.impl.PersistentNotifyingEntityServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class PersistentNotifyingEntityRepositoryTest {

    @Autowired
    private InvitationRepository persistenceNotificationRepository;
    @Autowired
    private PersistentNotifyingEntityServiceImpl persistenceNotificationService;
    private String name;
    private boolean isCompleted;


    @BeforeEach
    public void setUp() {
        this.name = "Mateusz";
        this.isCompleted = false;
    }

    @Test
    void shouldReturnNoCompletedNotificationsFromDB(){
        List<Invitation> result = persistenceNotificationRepository.findAllByNotificationDestinationUserAndCompleted(name, isCompleted);
        Assertions.assertNotEquals(0 , result.size());
    }

    @Test
    void shouldReturnNoCompletedNotificationsFromDBMinimum1(){
        List<PersistentNotifyingEntity> result = persistenceNotificationService.getAllPersistenceNotifications(name, isCompleted);
        Assertions.assertNotEquals(0 , result.size());
    }

}