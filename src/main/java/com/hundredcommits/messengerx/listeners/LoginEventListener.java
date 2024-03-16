package com.hundredcommits.messengerx.listeners;

import com.hundredcommits.messengerx.domains.User;
import com.hundredcommits.messengerx.dtos.UserWithFriendsDTO;
import com.hundredcommits.messengerx.service.UserService;
import com.hundredcommits.messengerx.utils.ActiveSessionManager;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class LoginEventListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    private final ActiveSessionManager activeSessionManager;
    private final UserService userService;

    public LoginEventListener(ActiveSessionManager activeSessionManager, UserService userService) {
        this.activeSessionManager = activeSessionManager;
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();
        User user = userService.findUserByUsername(username);
        if (user != null) {
            UserWithFriendsDTO userDTO = new UserWithFriendsDTO(username, user.getFriends());
            activeSessionManager.add(userDTO);
        }
    }
}
