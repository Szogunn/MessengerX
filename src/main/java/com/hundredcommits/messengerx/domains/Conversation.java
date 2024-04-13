package com.hundredcommits.messengerx.domains;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Conversation extends BaseEntity {
    private String chatId;
    private String userOneId;
    private String userTwoId;

    public Conversation(String chatId, String userOneId, String userTwoId) {
        this.chatId = chatId;
        this.userOneId = userOneId;
        this.userTwoId = userTwoId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getUserOneId() {
        return userOneId;
    }

    public void setUserOneId(String userOneId) {
        this.userOneId = userOneId;
    }

    public String getUserTwoId() {
        return userTwoId;
    }

    public void setUserTwoId(String userTwoId) {
        this.userTwoId = userTwoId;
    }
}
