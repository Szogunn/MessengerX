package com.hundredcommits.messengerx.service.impl;

import com.hundredcommits.messengerx.notification.StatusEvent;
import com.hundredcommits.messengerx.repositories.EmitterRepository;
import com.hundredcommits.messengerx.notification.EventNotify;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserSessionsEventNotifyImpl implements EventNotify<StatusEvent> {
    private final ThreadPoolExecutor notifyPool;
    private final EmitterRepository emitterRepository;

    public UserSessionsEventNotifyImpl(EmitterRepository emitterRepository) {
        this.emitterRepository = emitterRepository;
        notifyPool = new ThreadPoolExecutor(1, 5, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
    }

    @Override
    public void notify(String senderNotify, Set<String> recipientsNames, StatusEvent event) {
        notifyPool.submit(() -> recipientsNames.forEach(friend -> emitterRepository.get(friend).ifPresentOrElse(sseEmitter -> {
            try {
                log.info(String.format("Sending event: %s from member: %s to member: %s", event.getType(), senderNotify, friend));
                sseEmitter.send(SseEmitter.event().name(friend).data(event));
            } catch (IOException | IllegalStateException e) {
                log.error("Error while sending event: {} for member: {} - exception: {}", event.getType(), friend, e);
                emitterRepository.remove(friend);
            }
        }, () -> log.info("No emitter for member {}", friend)))); //tutaj mogę zaimplementować np zapisanie do bazy danyc powiadomienia zeby użytkownik mógł je odebrać po zalogowaniu się jeżeli nie jest aktywny
    }
}
