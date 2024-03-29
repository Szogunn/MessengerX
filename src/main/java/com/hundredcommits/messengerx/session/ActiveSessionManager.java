package com.hundredcommits.messengerx.session;

import com.hundredcommits.messengerx.notification.StatusEvent;
import com.hundredcommits.messengerx.payloads.FriendStatus;
import com.hundredcommits.messengerx.notification.EventNotify;
import com.hundredcommits.messengerx.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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

    public void add(String username) {
        map.put(username, PRESENT);
        notifyFriendsAboutStatusChanged(username, true);
    }

    public void remove(String username) {
        map.remove(username);
        notifyFriendsAboutStatusChanged(username, false);
    }

    public Set<String> getAll() {
        return map.keySet();
    }

    private void notifyFriendsAboutStatusChanged(String username, boolean online) {
        Set<String> onlineFriendsNames = findUserFriendsWithStatus(username)
                .stream()
                .filter(FriendStatus::status)
                .map(FriendStatus::username)
                .collect(Collectors.toSet());

        StatusEvent event = new StatusEvent(username, online);
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
