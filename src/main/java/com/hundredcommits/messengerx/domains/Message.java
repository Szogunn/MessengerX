package com.hundredcommits.messengerx.domains;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
public class Message extends PersistentNotifyingEntity {
    @Id
    private String id;
    private String conversationId;
    private String senderId;
    private String recipientId;
    private String content;
    private Date timestamp;
    private Date readTimestamp;

    public Message(String senderId, String recipientId, String content, Date timestamp) {
        super(recipientId);
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.content = content;
        this.timestamp = timestamp;
        this.readTimestamp = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public void setCompleted() {
        if (readTimestamp == null) {
            this.completed = true;
            this.readTimestamp = new Date(System.currentTimeMillis());
        }
    }

    public Date getReadTimestamp() {
        return readTimestamp;
    }
}
