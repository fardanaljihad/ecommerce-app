package com.example.ecommerce.consumer;

import java.text.NumberFormat;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.example.ecommerce.dto.OrderApprovedEvent;
import com.example.ecommerce.dto.OrderRejectedEvent;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationConsumer {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderConsumer.class);

    private final NotificationService notificationService;

    private final UserRepository userRepository;

    @KafkaListener(topics = "order-approved", groupId = "order-group")
    public void consumeOrderApproved(OrderApprovedEvent event) {

        LOGGER.info("[NotificationConsumer] Received OrderApproved Event: orderId={}", event.orderId());

        String paymentMethod = event.paymentMethod().name().replace("_", " ");
        String amount = NumberFormat.getInstance(new Locale("id", "ID")).format(event.amount());
        String message = String.format(
            "Payment for Order #%d of Rp%s via %s was successful!",
            event.orderId(),
            amount,
            paymentMethod
        );

        User user = userRepository.findById(event.userId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        notificationService.sendSuccessToUser(user.getUsername(), message);
    }

    @KafkaListener(topics = "order-rejected", groupId = "order-group")
    public void consumeOrderRejected(OrderRejectedEvent event) {

        LOGGER.info("[NotificationConsumer] Received OrderRejected Event: orderId={}", event.orderId());

        String paymentMethod = event.paymentMethod().name().replace("_", " ");
        String amount = NumberFormat.getInstance(new Locale("id", "ID")).format(event.amount());
        String reason = event.reason().replace("_", " ");
        String message = String.format(
            "Payment for Order #%d of Rp%s via %s failed: %s.",
            event.orderId(),
            amount,
            paymentMethod,
            reason
        );
        
        User user = userRepository.findById(event.userId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        notificationService.sendFailToUser(user.getUsername(), message);
    }
}
