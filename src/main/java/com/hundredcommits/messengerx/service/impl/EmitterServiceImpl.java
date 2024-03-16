package com.hundredcommits.messengerx.service.impl;

import com.hundredcommits.messengerx.repositories.EmitterRepository;
import com.hundredcommits.messengerx.service.EmitterService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class EmitterServiceImpl implements EmitterService {
    private final EmitterRepository repository;

    public EmitterServiceImpl(EmitterRepository repository) {
        this.repository = repository;
    }

    @Override
    public SseEmitter createEmitter(String memberId) {
        SseEmitter emitter = new SseEmitter();
        emitter.onCompletion(() -> repository.remove(memberId));
        emitter.onTimeout(() -> repository.remove(memberId));
        emitter.onError(e -> repository.remove(memberId));
        repository.addOrReplaceEmitter(memberId, emitter);
        return emitter;
    }
}
