package com.hundredcommits.messengerx.dtos;

import java.util.Set;

public record UserWithFriendsDTO(
        String username,
        Set<String> friendsNames
) {
}
