package com.example.ecommerce.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.ecommerce.dto.OrderApprovedEvent;
import com.example.ecommerce.dto.OrderRejectedEvent;
import com.example.ecommerce.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationConsumer {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderConsumer.class);

    private final NotificationService notificationService;

    @KafkaListener(topics = "order-approved", groupId = "order-group")
    public void consumeOrderApproved(OrderApprovedEvent event) {

        LOGGER.info("[NotificationConsumer] Received OrderApproved Event: orderId={}", event.orderId());

        String paymentMethod = event.paymentMethod().name().replace("_", " ");
        String message = String.format(
            "Payment Order#%d of Rp%d via %s was successful.",
            event.orderId(),
            event.amount(),
            paymentMethod
        );

        notificationService.sendToUser(event.userId(), message);
    }

    @KafkaListener(topics = "order-rejected", groupId = "order-group")
    public void consumeOrderRejected(OrderRejectedEvent event) {

        LOGGER.info("[NotificationConsumer] Received OrderRejected Event: orderId={}", event.orderId());

        String paymentMethod = event.paymentMethod().name().replace("_", " ");
        String reason = event.reason().replace("_", " ");
        String message = String.format(
            "Payment Order#%d of Rp%d via %s has failed. Reason: %s",
            event.orderId(),
            event.amount(),
            paymentMethod,
            reason
        );
        
        notificationService.sendToUser(event.userId(), message);
    }
}
