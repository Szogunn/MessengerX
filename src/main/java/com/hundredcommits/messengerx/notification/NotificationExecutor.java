package com.hundredcommits.messengerx.notification;

import com.hundredcommits.messengerx.repositories.EmitterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class NotificationExecutor implements EventNotify {
    private final ThreadPoolExecutor notifyPool;
    private final EmitterRepository emitterRepository;

    public NotificationExecutor(EmitterRepository emitterRepository) {
        this.emitterRepository = emitterRepository;
        notifyPool = new ThreadPoolExecutor(1, 5, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
    }

    @Override
    public void notify(Event event) {
        notifyPool.submit(() -> event.getRecipientsNames().forEach(friend -> emitterRepository.get(friend).ifPresentOrElse(sseEmitter -> {
            try {
                log.info(String.format("Sending event: %s from member: %s to member: %s", event.getType(), event.getSender(), friend));
                sseEmitter.send(SseEmitter.event().name(friend).data(event));
            } catch (IOException | IllegalStateException e) {
                log.error("Error while sending event: {} for member: {} - exception: {}", event.getType(), friend, e);
                emitterRepository.remove(friend);
            }
        }, () -> {
            log.info("No emitter for member {}", friend);//todo zamiast dodawać informacje o errorach lepiej jest rzucić customwym wyjątkiem, który następnie trzeba obsłużyć używając tej metody
        })));
    }
}
