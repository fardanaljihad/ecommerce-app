package com.example.ecommerce.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.ecommerce.dto.OrderEvent;

@Service
public class OrderLineItemService {
    
    @KafkaListener(topics = "order-created", groupId = "order-line-item-group")
    public void consume(OrderEvent event) {
        System.out.println("<<< [ORDER LINE ITEM] Recevied event");
        
        // TODO
    }
}
