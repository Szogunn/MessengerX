package com.hundredcommits.messengerx.service.impl;

import com.hundredcommits.messengerx.domains.Invitation;
import com.hundredcommits.messengerx.notification.EventNotify;
import com.hundredcommits.messengerx.notification.FriendRequestEvent;
import com.hundredcommits.messengerx.notification.NotificationExecutor;
import com.hundredcommits.messengerx.repositories.InvitationRepository;
import com.hundredcommits.messengerx.service.InvitationService;
import com.hundredcommits.messengerx.utils.AppUtil;
import com.hundredcommits.messengerx.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class InvitationServiceImpl implements InvitationService, EventNotify<FriendRequestEvent> {
    private final InvitationRepository invitationRepository;

    private final NotificationExecutor notificationExecutor;

    public InvitationServiceImpl(InvitationRepository invitationRepository, NotificationExecutor notificationExecutor) {
        this.invitationRepository = invitationRepository;
        this.notificationExecutor = notificationExecutor;
    }

    @Override
    public boolean responseForInvitation(boolean isAccepted, String inviteeUser) {
        String invitedUser = SecurityUtils.getAuthenticatedUsername();
        Optional<Invitation> optionalInvitation = invitationRepository.findByFromUserAndToUser(inviteeUser, invitedUser);
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

        return true;
    }

    @Override
    public void inviteUser(String newFriendUsername) {
        String authUser = SecurityUtils.getAuthenticatedUsername();
        if (AppUtil.isEmpty(newFriendUsername) || newFriendUsername.equals(authUser)) {
            log.warn("Provided name of user is empty or equals authenticated user name");
            return;
        }

        if (checkInvitationValid(authUser, newFriendUsername)) {
            return;
        }

        try {
            Invitation invitation = new Invitation(authUser, newFriendUsername);
            invitationRepository.save(invitation);
        } catch (Exception ex) {
            return;
        }

        FriendRequestEvent event = new FriendRequestEvent(authUser, newFriendUsername);
        notify(authUser, Set.of(newFriendUsername), event);
    }

    public boolean checkInvitationValid(String invitationFromUser, String invitationToUser) {
        Optional<Invitation> optionalInvitation = invitationRepository.findByFromUserAndToUser(invitationFromUser, invitationToUser);
        if (optionalInvitation.isEmpty()) {
            return false;
        }

        Invitation invitation = optionalInvitation.get();
        return invitation.getResponseDate() == null;
    }

    @Override
    public void notify(String senderNotify, Set<String> recipientsNames, FriendRequestEvent event) {
        List<String> errors = notificationExecutor.notify(senderNotify, recipientsNames, event);

        if (!errors.isEmpty()){
            for (String string : errors) {
                //todo: obsłużyć wyjątki powstałe podczas przesyłania eventów
            }
        }
    }
}
