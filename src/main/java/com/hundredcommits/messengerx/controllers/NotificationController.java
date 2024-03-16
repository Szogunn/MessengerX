package com.hundredcommits.messengerx.controllers;

import com.hundredcommits.messengerx.service.EmitterService;
import com.hundredcommits.messengerx.utils.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
@RequestMapping("/notification")
public class NotificationController {
    private final EmitterService emitterService;

    public NotificationController(EmitterService emitterService) {
        this.emitterService = emitterService;
    }

    @GetMapping("/subscribe")
    public SseEmitter subscribeToEvents() {
        return emitterService.createEmitter(SecurityUtils.getAuthenticatedUsername());
    }

    @GetMapping(value = "")
    public String getNotifications() {
        return "notification/index";
    }
}
