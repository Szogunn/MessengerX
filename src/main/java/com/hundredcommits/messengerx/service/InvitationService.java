package com.hundredcommits.messengerx.service;

import com.hundredcommits.messengerx.dtos.InvitationDTO;

public interface InvitationService {

    boolean responseForInvitation(boolean isAccepted, InvitationDTO invitationDTO);
    boolean saveInvitation(InvitationDTO invitationDTO);
    boolean checkInvitationValid(InvitationDTO invitationDTO);

}
