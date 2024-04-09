package com.hundredcommits.messengerx.service;

public interface InvitationService {

    boolean responseForInvitation(boolean isAccepted, String inviteeUser);
    void inviteUser(String newFriendUsername);

}
