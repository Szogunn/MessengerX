package com.hundredcommits.messengerx.domains;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
public class Invitation extends PersistentNotifyingEntity {
    private final Date invitationDate;
    private final String fromUser;
    private final String toUser;
    private boolean accepted;
    private Date responseDate;

    private Invitation() {
        super(null, null, null);
        this.invitationDate = null;
        this.fromUser = null;
        this.toUser = null;
    }

    public Invitation(String fromUser, String toUser) {
        super(toUser, fromUser, PersistentNotifyType.INVITATION);
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.invitationDate = new Date(System.currentTimeMillis());
        this.accepted = false;
        this.responseDate = null;
    }

    private Invitation(String id, String fromUser, String toUser, Date invitationDate, boolean accepted, Date responseDate) {
        super(toUser, fromUser, PersistentNotifyType.INVITATION);
        this.id = id;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.invitationDate = invitationDate;
        this.accepted = accepted;
        this.responseDate = responseDate;
    }

    public Invitation withInvitationDate(Date invitationDate) {
        return new Invitation(this.id, this.fromUser, this.toUser, invitationDate, this.accepted, this.responseDate);
    }

    public Invitation withFromUser(String fromUser) {
        return new Invitation(this.id, fromUser, this.toUser, this.invitationDate, this.accepted, this.responseDate);
    }

    public Invitation withToUser(String toUser) {
        return new Invitation(this.id, this.fromUser, toUser, this.invitationDate, this.accepted, this.responseDate);
    }

    public String getFromUser() {
        return fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public Date getInvitationDate() {
        return invitationDate;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        if (this.responseDate != null){
            return;
        }

        this.accepted = accepted;
        this.setCompleted(new Date());
        this.responseDate = new Date(System.currentTimeMillis());
    }

    public Date getResponseDate() {
        return responseDate;
    }

    @Override
    public void setCompleted(Date date) {
        if (this.responseDate != null){
            return;
        }

        this.completed = true;
    }
}
