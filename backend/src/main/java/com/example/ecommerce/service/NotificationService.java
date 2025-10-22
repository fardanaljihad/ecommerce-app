package com.example.ecommerce.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
    
    private final SimpMessagingTemplate messagingTemplate;

    public void sendToUser(Long userId, String message) {
        String destination = "/user/notifications/" + userId;
        messagingTemplate.convertAndSend(destination, message);
    }
}
