package com.hundredcommits.messengerx.controllers;

import com.hundredcommits.messengerx.payloads.FriendStatus;
import com.hundredcommits.messengerx.dtos.UserDTO;
import com.hundredcommits.messengerx.payloads.UnreadMessagesFromFriend;
import com.hundredcommits.messengerx.service.InvitationService;
import com.hundredcommits.messengerx.service.MessageService;
import com.hundredcommits.messengerx.service.UserService;
import com.hundredcommits.messengerx.session.ActiveSessionManager;
import com.hundredcommits.messengerx.utils.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/friends")
public class FriendsController {

    public static final String REDIRECT_FRIENDS = "redirect:/friends";
    private final UserService userService;
    private final InvitationService invitationService;
    private final ActiveSessionManager activeSessionManager;
    private final MessageService messageService;

    public FriendsController(UserService userService, InvitationService invitationService, ActiveSessionManager activeSessionManager, MessageService messageService) {
        this.userService = userService;
        this.invitationService = invitationService;
        this.activeSessionManager = activeSessionManager;
        this.messageService = messageService;
    }

    @GetMapping("")
    public String getFriends(Model model) {
        String username = SecurityUtils.getAuthenticatedUsername();
        Set<FriendStatus> friends = activeSessionManager.findUserFriendsWithStatus(username);

        model.addAttribute("friends", friends);
        Map<String, Long> unreadMessagesFromFriends = messageService.countUnreadMessagesPerFriend(username)
                .stream()
                .collect(Collectors.toMap(UnreadMessagesFromFriend::getUsername, UnreadMessagesFromFriend::getUnreadMessages));
        model.addAttribute("unreadMessages", unreadMessagesFromFriends);
        return "friends/index";
    }

    @PostMapping("/remove")
    public String removeFriend(@ModelAttribute UserDTO userDTO, Model model) {
        List<String> exceptions = new ArrayList<>();
        userService.removeFriend(userDTO.username(), exceptions);

        if (!exceptions.isEmpty()){
            model.addAttribute("exceptions", exceptions);
        }

        return REDIRECT_FRIENDS;
    }

    @PostMapping("/invite")
    public String inviteFriend(@ModelAttribute UserDTO userDTO) {
        invitationService.inviteUser(userDTO.username());

        return REDIRECT_FRIENDS;
    }

    @PostMapping("/response/{isAccepted}")
    public ResponseEntity<Boolean> responseForInvitation(@PathVariable String isAccepted, @RequestBody UserDTO userDTO) {
        boolean result = invitationService.responseForInvitation(Boolean.parseBoolean(isAccepted), userDTO.username());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
