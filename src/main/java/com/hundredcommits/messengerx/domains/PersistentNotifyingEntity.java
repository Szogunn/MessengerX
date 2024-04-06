package com.hundredcommits.messengerx.domains;

public abstract class PersistentNotifyingEntity {

    protected String notificationDestinationUser;
    protected boolean completed;

    protected PersistentNotifyingEntity(String notificationDestinationUser) {
        this.notificationDestinationUser = notificationDestinationUser;
        this.completed = false;
    }

    abstract void setCompleted();

    public boolean getCompleted(){
        return this.completed;
    }

    public String getNotificationDestinationUser() {
        return notificationDestinationUser;
    }
}
