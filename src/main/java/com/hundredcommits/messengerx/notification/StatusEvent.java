package com.hundredcommits.messengerx.notification;

import java.util.Map;

public class StatusEvent extends Event {
    private Map<String, Object> body;
    public StatusEvent(Map<String, Object> body) {
        super(EventType.USER_STATUS);
        this.body = body;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public void setBody(Map<String, Object> body) {
        this.body = body;
    }
}
