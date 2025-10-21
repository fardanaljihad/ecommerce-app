package com.example.ecommerce.consumer;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.example.ecommerce.dto.InsufficientStockEvent;
import com.example.ecommerce.dto.OrderApprovedEvent;
import com.example.ecommerce.dto.OrderRejectedEvent;
import com.example.ecommerce.dto.PaymentAuthorizedEvent;
import com.example.ecommerce.dto.PaymentFailedEvent;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.OrderStatus;
import com.example.ecommerce.producer.EventProducer;
import com.example.ecommerce.repository.OrderRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderConsumer {

    private final OrderRepository orderRepository;

    private final EventProducer eventProducer;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderConsumer.class);
    
    @KafkaListener(topics = "payment-authorized", groupId = "order-group")
    @Transactional
    public void consumePaymentAuthorized(PaymentAuthorizedEvent event) {
        LOGGER.info("[OrderConsumer] Received PaymentAuthorized Event: orderId={}", event.orderId());

        Order order = orderRepository.findById(event.orderId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        order.setStatus(OrderStatus.APPROVED);
        order.setUpdatedAt(new Date());
        orderRepository.save(order);

        OrderApprovedEvent approvedEvent = new OrderApprovedEvent(
            order.getId(), order.getUser().getId(), event.amount(), event.paymentMethod());

        eventProducer.sendMessage("order-approved", approvedEvent);
    }

    @KafkaListener(topics = "stock-reservation-failed", groupId = "order-group")
    @Transactional
    public void consumeStockReservationFailed(InsufficientStockEvent event) {
        LOGGER.info("[OrderConsumer] Received StockReservationFailed Event: orderId={}", event.orderId());

        Order order = orderRepository.findById(event.orderId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        order.setStatus(OrderStatus.REJECTED);
        order.setUpdatedAt(new Date());
        orderRepository.save(order);

        OrderRejectedEvent rejectedEvent = new OrderRejectedEvent(
            order.getId(), order.getUser().getId(), event.amount(), event.paymentMethod(), "INSUFFICIENT_STOCK");
  
        eventProducer.sendMessage("order-rejected", rejectedEvent);    
    }

    @KafkaListener(topics = "payment-failed", groupId = "order-group")
    @Transactional
    public void consumePaymentFailed(PaymentFailedEvent event) {
        LOGGER.info("[OrderConsumer] Received PaymentFailed Event: orderId={}", event.orderId());

        Order order = orderRepository.findById(event.orderId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        order.setStatus(OrderStatus.REJECTED);
        order.setUpdatedAt(new Date());
        orderRepository.save(order);

        OrderRejectedEvent rejectedEvent = new OrderRejectedEvent(
            order.getId(), order.getUser().getId(), event.amount(), event.paymentMethod(), event.reason());
 
        eventProducer.sendMessage("order-rejected", rejectedEvent); 
    }
}
