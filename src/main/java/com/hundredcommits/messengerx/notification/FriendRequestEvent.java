package com.hundredcommits.messengerx.notification;

public class FriendRequestEvent extends Event {
    private final String fromUser;
    private final String toUser;

    public FriendRequestEvent(String fromUser, String toUser) {
        super(EventType.FRIEND_REQUEST);
        this.fromUser = fromUser;
        this.toUser = toUser;
    }

    public String getFromUser() {
        return fromUser;
    }

    public String getToUser() {
        return toUser;
    }
}
