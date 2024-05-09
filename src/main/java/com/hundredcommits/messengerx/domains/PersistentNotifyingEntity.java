package com.hundredcommits.messengerx.domains;

import java.util.Date;

public abstract class PersistentNotifyingEntity extends BaseEntity {

    protected String notificationDestinationUser;
    protected String notificationSenderUser;
    protected boolean completed;
    protected PersistentNotifyType persistentNotifyType;

    protected PersistentNotifyingEntity(String notificationDestinationUser, String notificationSenderUser, PersistentNotifyType persistentNotifyType) {
        this.notificationDestinationUser = notificationDestinationUser;
        this.notificationSenderUser = notificationSenderUser;
        this.completed = false;
        this.persistentNotifyType = persistentNotifyType;
    }

    public abstract void setCompleted(Date date);

    public boolean getCompleted(){
        return this.completed;
    }

    public String getNotificationDestinationUser() {
        return notificationDestinationUser;
    }

    public String getNotificationSenderUser() {
        return notificationSenderUser;
    }

    public PersistentNotifyType getPersistentNotifyType() {
        return persistentNotifyType;
    }

    protected enum PersistentNotifyType {
        INVITATION,
        MESSAGE,
    }
}
