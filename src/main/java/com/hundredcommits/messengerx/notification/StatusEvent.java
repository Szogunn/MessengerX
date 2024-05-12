package com.hundredcommits.messengerx.notification;

import java.util.Set;

public class StatusEvent extends Event {

    private final boolean online;

    public StatusEvent(String fromUser, Set<String> recipientsNames, boolean online) {
        super(fromUser, recipientsNames, EventType.USER_STATUS);
        this.online = online;
    }

    public boolean isOnline() {
        return online;
    }

}
