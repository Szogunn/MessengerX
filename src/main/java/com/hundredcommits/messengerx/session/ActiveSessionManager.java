package com.hundredcommits.messengerx.session;

import com.hundredcommits.messengerx.dtos.UserWithFriendsDTO;
import com.hundredcommits.messengerx.notification.StatusEvent;
import com.hundredcommits.messengerx.payloads.FriendStatus;
import com.hundredcommits.messengerx.notification.EventNotify;
import com.hundredcommits.messengerx.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ActiveSessionManager {
    private final Map<String, Object> map;
    private static final Object PRESENT = new Object();

    private final EventNotify<StatusEvent> eventNotify;
    private final UserService userService;

    public ActiveSessionManager(EventNotify<StatusEvent> eventNotify, UserService userService) {
        this.eventNotify = eventNotify;
        this.userService = userService;
        this.map = new ConcurrentHashMap<>();
    }

    public void add(UserWithFriendsDTO user) {
        map.put(user.username(), PRESENT);
        notifyFriendsAboutStatusChanged(user.username(), true);
    }

    public void remove(UserWithFriendsDTO user) {
        map.remove(user.username());
        notifyFriendsAboutStatusChanged(user.username(), false);
    }

    public Set<String> getAll() {
        return map.keySet();
    }

    private void notifyFriendsAboutStatusChanged(String username, boolean online) {
        Set<String> onlineFriendsNames = findUserFriendsWithStatus(username).stream()
                .filter(FriendStatus::status)
                .map(FriendStatus::username)
                .collect(Collectors.toSet());

        Map<String, Object> body = new HashMap<>();
        body.put("username", username);
        body.put("online", online);
        StatusEvent event = new StatusEvent(body);
        eventNotify.notify(username, onlineFriendsNames, event);
    }

    public Set<FriendStatus> findUserFriendsWithStatus(String username) {
        Set<String> userFriendsNames = userService.findUserFriendsName(username);
        Set<String> allActiveUsers = getAll();
        return userFriendsNames.stream()
                .map(name -> new FriendStatus(name, allActiveUsers.contains(name)))
                .collect(Collectors.toSet());
    }
}
