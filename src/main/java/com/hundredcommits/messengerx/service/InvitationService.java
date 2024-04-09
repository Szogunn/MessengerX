package com.hundredcommits.messengerx.service;

import com.hundredcommits.messengerx.dtos.InvitationDTO;

public interface InvitationService {

    boolean responseForInvitation(boolean isAccepted, InvitationDTO invitationDTO);
    void inviteUser(String username);

}
