package com.example.ecommerce.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
    
    private final SimpMessagingTemplate messagingTemplate;

    public void sendSuccessToUser(String username, String message) {
        String destination = "/user/notifications/success/" + username;
        messagingTemplate.convertAndSend(destination, message);
    }

    public void sendFailToUser(String username, String message) {
        String destination = "/user/notifications/fail/" + username;
        messagingTemplate.convertAndSend(destination, message);
    }
}
