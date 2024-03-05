package com.hundredcommits.messengerx.payloads;

public record SignupRequest(
        String username,
        String email,
        String password
) {
}
