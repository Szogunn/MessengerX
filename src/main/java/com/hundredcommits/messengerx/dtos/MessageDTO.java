package com.hundredcommits.messengerx.dtos;

public record MessageDTO(
        String messageId,
        String content,
        String senderId
        ) {
}
