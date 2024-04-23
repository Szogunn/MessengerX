package com.hundredcommits.messengerx.domains;

public abstract class PersistentNotifyingEntity extends BaseEntity {

    protected String notificationDestinationUser;
    protected String notificationSenderUser;
    protected boolean completed;

    protected PersistentNotifyingEntity(String notificationDestinationUser, String notificationSenderUser) {
        this.notificationDestinationUser = notificationDestinationUser;
        this.notificationSenderUser = notificationSenderUser;
        this.completed = false;
    }

    public abstract void setCompleted();

    public boolean getCompleted(){
        return this.completed;
    }

    public String getNotificationDestinationUser() {
        return notificationDestinationUser;
    }

    public String getNotificationSenderUser() {
        return notificationSenderUser;
    }
}
