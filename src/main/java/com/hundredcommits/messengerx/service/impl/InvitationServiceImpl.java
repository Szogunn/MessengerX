package com.hundredcommits.messengerx.service.impl;

import com.hundredcommits.messengerx.domains.Invitation;
import com.hundredcommits.messengerx.dtos.InvitationDTO;
import com.hundredcommits.messengerx.repositories.InvitationRepository;
import com.hundredcommits.messengerx.service.InvitationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class InvitationServiceImpl implements InvitationService {
    private final InvitationRepository invitationRepository;

    public InvitationServiceImpl(InvitationRepository invitationRepository) {
        this.invitationRepository = invitationRepository;
    }

    @Override
    public boolean responseForInvitation(boolean isAccepted, InvitationDTO invitationDTO) {
        Optional<Invitation> optionalInvitation = invitationRepository.findByFromUserAndToUser(invitationDTO.fromUser(), invitationDTO.toUser());
        if (optionalInvitation.isEmpty() || optionalInvitation.get().getResponseDate() != null){
            log.error("Not found invitation with given information", invitationDTO);
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
    public boolean saveInvitation(InvitationDTO invitationDTO) {
        if (checkInvitationValid(invitationDTO)) {
            return false;
        }

        try {
            Invitation invitation = new Invitation(invitationDTO.fromUser(), invitationDTO.toUser());
            invitationRepository.save(invitation);
        } catch (Exception ex) {
            return false;
        }

        return true;
    }

    @Override
    public boolean checkInvitationValid(InvitationDTO invitationDTO) {
        Optional<Invitation> optionalInvitation = invitationRepository.findByFromUserAndToUser(invitationDTO.fromUser(), invitationDTO.toUser());
        if (optionalInvitation.isEmpty()) {
            return false;
        }

        Invitation invitation = optionalInvitation.get();
        return invitation.getResponseDate() == null;
    }
}
