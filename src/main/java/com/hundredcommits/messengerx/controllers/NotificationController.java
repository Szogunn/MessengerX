package com.hundredcommits.messengerx.controllers;

import com.hundredcommits.messengerx.domains.PersistentNotifyingEntity;
import com.hundredcommits.messengerx.service.EmitterService;
import com.hundredcommits.messengerx.service.impl.PersistentNotifyingEntityServiceImpl;
import com.hundredcommits.messengerx.utils.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Controller
@RequestMapping("/notification")
public class NotificationController {
    private final EmitterService emitterService;
    private final PersistentNotifyingEntityServiceImpl persistenceNotificationService;

    public NotificationController(EmitterService emitterService, PersistentNotifyingEntityServiceImpl persistenceNotificationService) {
        this.emitterService = emitterService;
        this.persistenceNotificationService = persistenceNotificationService;
    }

    @GetMapping("/subscribe")
    public SseEmitter subscribeToEvents() {
        return emitterService.createEmitter(SecurityUtils.getAuthenticatedUsername());
    }

    @GetMapping(value = "")
    public String getNotifications() {
        return "notification/index";
    }

    @GetMapping(path = "/unread")
    public String getUnreadNotifications(Model model) {
        List<PersistentNotifyingEntity> notifications = persistenceNotificationService.getAllPersistenceNotifications(SecurityUtils.getAuthenticatedUsername(), false);

        model.addAttribute("notifications", notifications);

        return "notification/index";
    }

}
