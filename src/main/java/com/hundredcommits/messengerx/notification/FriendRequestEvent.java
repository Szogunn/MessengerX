package com.hundredcommits.messengerx.notification;

public class FriendRequestEvent extends Event {
    private final String username;

    public FriendRequestEvent(String username) {
        super(EventType.FRIEND_REQUEST);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
