package com.hundredcommits.messengerx.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface EmitterService {
    SseEmitter createEmitter(String memberId);
}
