package com.hundredcommits.messengerx.repositories.impl;

import com.hundredcommits.messengerx.repositories.EmitterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Slf4j
public class EmitterRepositoryImpl implements EmitterRepository {

    private final Map<String, SseEmitter> userEmitterMap = new ConcurrentHashMap<>();

    @Override
    public void addOrReplaceEmitter(String memberId, SseEmitter emitter) {
        log.info("adding member {} into map {}", memberId, userEmitterMap.keySet());
        userEmitterMap.put(memberId, emitter);
    }

    @Override
    public void remove(String memberId) {
        if (memberId != null){
            log.info("removing member {} from map {}", memberId, userEmitterMap.keySet());
            userEmitterMap.remove(memberId);
        }
    }

    @Override
    public Optional<SseEmitter> get(String memberId) {
        return Optional.ofNullable(userEmitterMap.get(memberId));
    }
}
