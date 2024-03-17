package com.hundredcommits.messengerx.listeners;

import com.hundredcommits.messengerx.domains.User;
import com.hundredcommits.messengerx.dtos.UserWithFriendsDTO;
import com.hundredcommits.messengerx.service.UserService;
import com.hundredcommits.messengerx.utils.ActiveSessionManager;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class LogoutEventListener implements ApplicationListener<LogoutSuccessEvent> {
    private final ActiveSessionManager activeSessionManager;
    private final UserService userService;

    public LogoutEventListener(ActiveSessionManager activeSessionManager, UserService userService) {
        this.activeSessionManager = activeSessionManager;
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(LogoutSuccessEvent event) {
        String username = event.getAuthentication().getName();
        User user = userService.findUserByUsername(username);
        if (user != null) {
            UserWithFriendsDTO userDTO = new UserWithFriendsDTO(username, user.getFriends());
            activeSessionManager.remove(userDTO);
        }
    }
}
