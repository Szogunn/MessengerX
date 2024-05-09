package com.hundredcommits.messengerx.dtos;

import java.util.Date;

public record ReadMessageDTO (
        String messageId,
        Date readTimestamp
){
}
