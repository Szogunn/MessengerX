package com.hundredcommits.messengerx.notification;

import java.util.Set;

public interface EventNotify <T extends Event> {

    void notify(String senderNotify, Set<String> recipientsNames, T event);
}
