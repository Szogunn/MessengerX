package com.hundredcommits.messengerx.payloads;

public class UnreadMessagesFromFriend {

    String username;
    Long unreadMessages;

    public UnreadMessagesFromFriend(String username, Long unreadMessages) {
        this.username = username;
        this.unreadMessages = unreadMessages;
    }

    public String getUsername() {
        return username;
    }

    public Long getUnreadMessages() {
        return unreadMessages;
    }
}
