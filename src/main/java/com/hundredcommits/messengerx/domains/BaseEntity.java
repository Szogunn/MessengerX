package com.hundredcommits.messengerx.domains;

import org.springframework.data.annotation.Id;

public abstract class BaseEntity {

    @Id
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
