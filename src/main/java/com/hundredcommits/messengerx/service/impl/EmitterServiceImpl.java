package com.hundredcommits.messengerx.service.impl;

import com.hundredcommits.messengerx.repositories.EmitterRepository;
import com.hundredcommits.messengerx.service.EmitterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@Slf4j
public class EmitterServiceImpl implements EmitterService {
    private final EmitterRepository repository;

    public EmitterServiceImpl(EmitterRepository repository) {
        this.repository = repository;
    }

    @Override
    public SseEmitter createEmitter(String memberId) {
        SseEmitter emitter = new SseEmitter(86400000L);
        emitter.onCompletion(() -> repository.remove(memberId));
        emitter.onTimeout(() -> repository.remove(memberId)); // można ustawić timeout, żeby wysyłał wiadomość o timeoucie co klient będzie odbierał i odnawiał połącznie jeżeli jest ciągle aktywny
        emitter.onError(e -> repository.remove(memberId));
        repository.addOrReplaceEmitter(memberId, emitter);
        return emitter;
    }
}
