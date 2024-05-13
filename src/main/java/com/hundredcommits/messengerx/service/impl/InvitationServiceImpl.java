package com.hundredcommits.messengerx.service.impl;

import com.hundredcommits.messengerx.domains.Invitation;
import com.hundredcommits.messengerx.notification.*;
import com.hundredcommits.messengerx.repositories.InvitationRepository;
import com.hundredcommits.messengerx.service.InvitationService;
import com.hundredcommits.messengerx.service.UserService;
import com.hundredcommits.messengerx.utils.AppUtil;
import com.hundredcommits.messengerx.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class InvitationServiceImpl implements InvitationService {

    private final UserService userService;
    private final InvitationRepository invitationRepository;

    private final EventNotify eventNotify;

    public InvitationServiceImpl(UserService userService, InvitationRepository invitationRepository, EventNotify eventNotify) {
        this.userService = userService;
        this.invitationRepository = invitationRepository;
        this.eventNotify = eventNotify;
    }

    @Override
    public boolean responseForInvitation(boolean isAccepted, String inviteeUser) {
        String invitedUser = SecurityUtils.getAuthenticatedUsername();
        Optional<Invitation> optionalInvitation = invitationRepository.findByFromUserAndToUserAndCompleted(inviteeUser, invitedUser, false);
        if (optionalInvitation.isEmpty() || optionalInvitation.get().getResponseDate() != null){
            log.error(String.format("Not found invitation from user %s to user %s.", inviteeUser, invitedUser));
            return false;
        }

        try {
            Invitation invitation = optionalInvitation.get();
            invitation.setAccepted(isAccepted);
            invitationRepository.save(invitation);
        } catch (Exception ex){
            return false;
        }

        if (isAccepted){
            userService.addFriend(inviteeUser, List.of());
        }

        FriendResponseEvent friendResponseEvent = new FriendResponseEvent(inviteeUser, invitedUser, isAccepted);
        sendFriendResponseEvent(friendResponseEvent);
        return true;
    }

    @Override
    public void inviteUser(String newFriendUsername) {
        String authUser = SecurityUtils.getAuthenticatedUsername();
        if (AppUtil.isEmpty(newFriendUsername) || newFriendUsername.equals(authUser)) {
            log.warn("Provided name of user is empty or equals authenticated user name");
            return;
        }

        if (!checkInvitationInvalid(authUser, newFriendUsername)) {
            return;
        }

        try {
            Invitation invitation = new Invitation(authUser, newFriendUsername);
            invitationRepository.save(invitation);
        } catch (Exception ex) {
            return;
        }

        FriendRequestEvent event = new FriendRequestEvent(authUser, newFriendUsername);
        sendFriendRequestEvent(event);
    }

    public boolean checkInvitationInvalid(String invitationFromUser, String invitationToUser) {
        Optional<Invitation> existingInvitation = invitationRepository.findByFromUserAndToUserAndCompleted(invitationFromUser, invitationToUser, false);
        if (existingInvitation.isEmpty()) {
            log.debug("Invitation does not exist");
            return true;
        }

        Invitation invitation = existingInvitation.get();
        if (invitation.getResponseDate() == null) {
            log.debug("Invitation exist but has not been completed");
            return false;
        }

        return !userService.isUsersAreFriends(invitationFromUser, invitationToUser);
    }

    @Override
    public void sendFriendRequestEvent(FriendRequestEvent event) {
        eventNotify.notify(event);
    }

    @Override
    public void sendFriendResponseEvent(FriendResponseEvent event) {
        eventNotify.notify(event);
    }
}
