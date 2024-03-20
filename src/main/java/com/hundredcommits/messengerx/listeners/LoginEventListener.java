package com.hundredcommits.messengerx.listeners;

import com.hundredcommits.messengerx.session.ActiveSessionManager;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class LoginEventListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    private final ActiveSessionManager activeSessionManager;

    public LoginEventListener(ActiveSessionManager activeSessionManager) {
        this.activeSessionManager = activeSessionManager;
    }

    @Override
    public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();
        activeSessionManager.add(username);
    }
}
