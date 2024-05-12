package com.hundredcommits.messengerx.service;

import com.hundredcommits.messengerx.notification.FriendRequestEvent;
import com.hundredcommits.messengerx.notification.FriendResponseEvent;

public interface InvitationService {

    boolean responseForInvitation(boolean isAccepted, String inviteeUser);
    void inviteUser(String newFriendUsername);
    void sendFriendRequestEvent(FriendRequestEvent event);
    void sendFriendResponseEvent(FriendResponseEvent event);

}
