package com.hundredcommits.messengerx.payloads;

public record JwtInfoResponse(
        String username,
        String token
) {
}
