package com.example.ecommerce.consumer;

import java.util.Date;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.example.ecommerce.dto.InsufficientStockEvent;
import com.example.ecommerce.dto.OrderEvent;
import com.example.ecommerce.dto.PaymentAuthorizedEvent;
import com.example.ecommerce.dto.PaymentCancelledEvent;
import com.example.ecommerce.dto.PaymentFailedEvent;
import com.example.ecommerce.dto.StockReservedEvent;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.Payment;
import com.example.ecommerce.producer.EventProducer;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.PaymentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentConsumer {
    
    private final OrderRepository orderRepository;

    private final PaymentRepository paymentRepository;

    private final EventProducer eventProducer;

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentConsumer.class);
    
    @KafkaListener(topics = "order-created", groupId = "payment-group")
    @Transactional
    public void consumeOrderCreated(OrderEvent event) {
        LOGGER.info("[OrderLineItemConsumer] Received OrderCreated Event: orderId={}", event.orderId());

        Order order = orderRepository.findById(event.orderId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(event.amount());
        payment.setPaymentMethod(event.paymentMethod());
        payment.setStatus("PENDING");
        payment.setCreatedAt(new Date());

        paymentRepository.save(payment);
    }

    @KafkaListener(topics = "stock-reserved", groupId = "payment-group")
    @Transactional
    public void consumeStockReserved(StockReservedEvent event) {
        LOGGER.info("[OrderLineItemConsumer] Received StockReserved Event: orderId={}", event.orderId());

        Payment payment = paymentRepository.findByOrder_Id(event.orderId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));

        boolean success = new Random().nextBoolean();

        if (success) {
            payment.setStatus("SUCCESS");
            payment.setUpdatedAt(new Date());
            paymentRepository.save(payment);

            PaymentAuthorizedEvent authorizedEvent = new PaymentAuthorizedEvent(
                payment.getId(), event.orderId(), event.amount(), payment.getPaymentMethod(), event.orderLineItems());

            eventProducer.sendMessage("payment-authorized", authorizedEvent);

        } else {
            payment.setStatus("FAILED");
            payment.setUpdatedAt(new Date());
            paymentRepository.save(payment);

            PaymentFailedEvent paymentFailedEvent = new PaymentFailedEvent(
                payment.getId(), payment.getOrder().getId(), event.amount(), payment.getPaymentMethod(),event.reservedStocks(),"INSUFFICIENT_FUNDS");

            eventProducer.sendMessage("payment-failed", paymentFailedEvent);
        }
    }

    @KafkaListener(topics = "stock-reservation-failed", groupId = "payment-group")
    @Transactional
    public void consumeStockReservationFailed(InsufficientStockEvent event) {
        LOGGER.info("[OrderLineItemConsumer] Received StockReservationFailed Event: orderId={}", event.orderId());

        Payment payment = paymentRepository.findByOrder_Id(event.orderId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));

        payment.setStatus("CANCELLED");
        payment.setUpdatedAt(new Date());
        paymentRepository.save(payment);

        PaymentCancelledEvent paymentCancelledEvent = new PaymentCancelledEvent(
            payment.getId(), payment.getOrder().getId(), "INSUFFICIENT_STOCK");

        eventProducer.sendMessage("payment-cancelled", paymentCancelledEvent);
    }
}
