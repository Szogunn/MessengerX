package com.hundredcommits.messengerx.notification;

public abstract class Event {
    private final EventType type;

    protected Event(EventType eventType) {
        this.type = eventType;
    }

    public String getType() {
        return type.toString();
    }

    public enum EventType {
        USER_STATUS
    }
}
