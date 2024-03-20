package com.hundredcommits.messengerx.listeners;

import com.hundredcommits.messengerx.repositories.EmitterRepository;
import com.hundredcommits.messengerx.session.ActiveSessionManager;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class LogoutEventListener implements ApplicationListener<LogoutSuccessEvent> {
    private final ActiveSessionManager activeSessionManager;
    private final EmitterRepository emitterRepository;

    public LogoutEventListener(ActiveSessionManager activeSessionManager, EmitterRepository emitterRepository) {
        this.activeSessionManager = activeSessionManager;
        this.emitterRepository = emitterRepository;
    }

    @Override
    public void onApplicationEvent(LogoutSuccessEvent event) {
        String username = event.getAuthentication().getName();
        activeSessionManager.remove(username);
        emitterRepository.remove(username);
    }
}
