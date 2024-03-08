package com.hundredcommits.messengerx.domains;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Conversation {

    @Id
    private String id;
    private String chatId;
    private String userOneId;
    private String userTwoId;

    public Conversation(String chatId, String userOneId, String userTwoId) {
        this.chatId = chatId;
        this.userOneId = userOneId;
        this.userTwoId = userTwoId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
