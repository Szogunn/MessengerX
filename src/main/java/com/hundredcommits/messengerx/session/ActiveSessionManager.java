package com.hundredcommits.messengerx.session;

import com.hundredcommits.messengerx.notification.StatusEvent;
import com.hundredcommits.messengerx.payloads.FriendStatus;
import com.hundredcommits.messengerx.notification.EventNotify;
import com.hundredcommits.messengerx.service.UserService;
import com.hundredcommits.messengerx.notification.NotificationExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ActiveSessionManager implements EventNotify<StatusEvent> {
    private final Map<String, Object> map;
    private static final Object PRESENT = new Object();

    private final UserService userService;
    private final NotificationExecutor notificationExecutor;

    public ActiveSessionManager(UserService userService, NotificationExecutor notificationExecutor) {
        this.userService = userService;
        this.notificationExecutor = notificationExecutor;
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
        notify(username, onlineFriendsNames, event);
    }

    public Set<FriendStatus> findUserFriendsWithStatus(String username) {
        Set<String> userFriendsNames = userService.findUserFriendsName(username);
        Set<String> allActiveUsers = getAll();
        return userFriendsNames.stream()
                .map(name -> new FriendStatus(name, allActiveUsers.contains(name)))
                .collect(Collectors.toSet());
    }

    @Override
    public void notify(String senderNotify, Set<String> recipientsNames, StatusEvent event) {
        List<String> notify = notificationExecutor.notify(senderNotify, recipientsNames, event);

        if (notify.isEmpty()){
            return;
        }

        for (String string : notify) {
            //todo: do obsłużenia kody błędu. Kody zaimplementować jako jakiś Enum
        }
    }
}
