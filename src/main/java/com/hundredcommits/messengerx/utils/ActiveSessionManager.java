package com.hundredcommits.messengerx.utils;

import com.hundredcommits.messengerx.dtos.UserWithFriendsDTO;
import com.hundredcommits.messengerx.payloads.Event;
import com.hundredcommits.messengerx.repositories.EmitterRepository;
import com.hundredcommits.messengerx.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ActiveSessionManager implements NotificationService {
    private final Map<String, Object> map;
    private static final Object PRESENT = new Object();
    private final ThreadPoolExecutor notifyPool;

    private final EmitterRepository emitterRepository;

    public ActiveSessionManager(EmitterRepository emitterRepository) {
        this.emitterRepository = emitterRepository;
        this.map = new ConcurrentHashMap<>();
        notifyPool = new ThreadPoolExecutor(1, 5, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
    }

    public void add(UserWithFriendsDTO user) {
        map.put(user.username(), PRESENT);
        notify(user);
    }

    public void remove(UserWithFriendsDTO user) {
        map.remove(user.username());
        notify(user);
    }

    public Set<String> getAll() {
        return map.keySet();
    }

    @Override
    public void notify(UserWithFriendsDTO senderNotify) {
        if (senderNotify == null){
            return;
        }

        Map<String, Object> body = new HashMap<>();
        String username = senderNotify.username();
        body.put("username", username);
        boolean online = getAll().contains(username);
        body.put("online", online);

        Event event = new Event(Event.EventType.USER_STATUS, body);
        doSendNotification(senderNotify, event);
    }

    private void doSendNotification(UserWithFriendsDTO senderNotify, Event event) {
        Set<String> friends = senderNotify.friendsNames();
        notifyPool.submit(() -> friends.forEach(friend -> emitterRepository.get(friend).ifPresentOrElse(sseEmitter -> {
            try {
                log.info("Sending event: {} for member: {}", event, friend);
                sseEmitter.send(SseEmitter.event().name(friend).data(event));
            } catch (IOException | IllegalStateException e) {
                log.info("Error while sending event: {} for member: {} - exception: {}", event, friend, e);
                emitterRepository.remove(friend);
            }
        }, () -> log.info("No emitter for member {}", friend)))); //tutaj mogę zaimplementować np zapisanie do bazy danyc powiadomienia zeby użytkownik mógł je odebrać po zalogowaniu się jeżeli nie jest aktywny
    }
}
