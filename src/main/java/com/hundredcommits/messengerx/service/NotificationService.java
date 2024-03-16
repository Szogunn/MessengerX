package com.hundredcommits.messengerx.service;

import com.hundredcommits.messengerx.dtos.UserWithFriendsDTO;

public interface NotificationService {

    void notify(UserWithFriendsDTO senderNotify);
}
