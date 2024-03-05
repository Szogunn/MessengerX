package com.hundredcommits.messengerx.payloads;

public record LoginRequest(
        String username,
        String password
) {
}
