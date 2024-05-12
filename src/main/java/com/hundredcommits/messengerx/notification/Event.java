package com.hundredcommits.messengerx.notification;

import java.util.Set;

public abstract class Event {
    private final String sender;
    private final Set<String> recipientsNames;
    private final EventType type;

    protected Event(String sender, Set<String> recipientsNames, EventType eventType) {
        this.sender = sender;
        this.recipientsNames = recipientsNames;
        this.type = eventType;
    }

    public String getType() {
        return type.toString();
    }

    public String getSender() {
        return sender;
    }

    public Set<String> getRecipientsNames() {
        return recipientsNames;
    }

    public enum EventType {
        USER_STATUS,
        FRIEND_REQUEST,
        FRIEND_RESPONSE,
    }
}
