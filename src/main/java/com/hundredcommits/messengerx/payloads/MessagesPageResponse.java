package com.hundredcommits.messengerx.payloads;

import com.hundredcommits.messengerx.domains.Message;

import java.util.List;

public record MessagesPageResponse (
        List<Message> content,
        int pageNo,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean isLast
){
    public static MessagesPageResponse EMPTY_RESPONSE = new MessagesPageResponse(List.of(), 0,0,0,0,true);
}
