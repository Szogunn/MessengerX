package com.hundredcommits.messengerx.notification;

import java.util.Set;

public class FriendResponseEvent extends Event {
    private final boolean isAccepted;

    public FriendResponseEvent(String fromUser, String toUser, boolean isAccepted) {
        super(fromUser, Set.of(toUser), EventType.FRIEND_RESPONSE);
        this.isAccepted = isAccepted;
    }
}
