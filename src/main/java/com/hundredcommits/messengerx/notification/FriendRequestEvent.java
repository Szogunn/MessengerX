package com.hundredcommits.messengerx.notification;

import java.util.Set;

public class FriendRequestEvent extends Event {

    public FriendRequestEvent(String fromUser, String toUser) {
        super(fromUser, Set.of(toUser), EventType.FRIEND_REQUEST);
    }

}
