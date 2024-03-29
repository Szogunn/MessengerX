package com.hundredcommits.messengerx.notification;

import com.hundredcommits.messengerx.utils.AppUtil;

public class StatusEvent extends Event {
    private final String username;
    private final boolean online;
    public StatusEvent(String username, boolean online) {
        super(EventType.USER_STATUS);

        if (!AppUtil.isEmpty(username)) {
            this.username = username;
            this.online = online;
        } else {
            this.username = "";
            this.online = false;
        }
    }

    public boolean isOnline() {
        return online;
    }

    public String getUsername() {
        return username;
    }
}
