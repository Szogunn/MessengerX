package com.hundredcommits.messengerx.payloads;

import java.io.Serializable;
import java.util.Map;

public class Event implements Serializable {
    private EventType type;

    public Event() {
    }

    private Map<String, Object> body;

    public Event(EventType type, Map<String, Object> body) {
        this.type = type;
        this.body = body;
    }

    public String getType() {
        return type.toString();
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public void setBody(Map<String, Object> body) {
        this.body = body;
    }

    public enum EventType{
        USER_STATUS
    }
}
