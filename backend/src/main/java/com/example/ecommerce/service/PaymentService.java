package com.example.ecommerce.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.ecommerce.dto.OrderEvent;

@Service
public class PaymentService {
    
    @KafkaListener(topics = "order-created", groupId = "payment-group")
    public void consume(OrderEvent event) {
        System.out.println("<<< [PAYMENT] Recevied event");
        System.out.println("<<< " + event);
        
        // TODO
    }
}
