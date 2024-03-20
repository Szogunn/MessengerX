package com.hundredcommits.messengerx.controllers;

import com.hundredcommits.messengerx.payloads.FriendStatus;
import com.hundredcommits.messengerx.dtos.UserDTO;
import com.hundredcommits.messengerx.service.UserService;
import com.hundredcommits.messengerx.session.ActiveSessionManager;
import com.hundredcommits.messengerx.utils.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/friends")
public class FriendsController {

    private final UserService userService;
    private final ActiveSessionManager activeSessionManager;

    public FriendsController(UserService userService, ActiveSessionManager activeSessionManager) {
        this.userService = userService;
        this.activeSessionManager = activeSessionManager;
    }

    @GetMapping("")
    public String getFriends(Model model) {
        String username = SecurityUtils.getAuthenticatedUsername();
        Set<FriendStatus> friends = activeSessionManager.findUserFriendsWithStatus(username);

        model.addAttribute("friends", friends);
        return "friends/index";
    }

    @PostMapping("/add")
    public String addFriend(@ModelAttribute UserDTO userDTO, Model model) {
        List<String> exceptions = new ArrayList<>();
        userService.addFriend(userDTO.username(), exceptions);

        if (!exceptions.isEmpty()){
            model.addAttribute("exceptions", exceptions);
        }

        return "redirect:/friends";
    }

    @PostMapping("/remove")
    public String removeFriend(@ModelAttribute UserDTO userDTO, Model model) {
        List<String> exceptions = new ArrayList<>();
        userService.removeFriend(userDTO.username(), exceptions);

        if (!exceptions.isEmpty()){
            model.addAttribute("exceptions", exceptions);
        }

        return "redirect:/friends";
    }
}
