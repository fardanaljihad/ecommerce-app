package com.example.ecommerce.consumer;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.ecommerce.dto.InsufficientStockEvent;
import com.example.ecommerce.dto.OrderEvent;
import com.example.ecommerce.dto.PaymentFailedEvent;
import com.example.ecommerce.dto.StockReservedEvent;
import com.example.ecommerce.producer.EventProducer;
import com.example.ecommerce.repository.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductConsumer {

    private final ProductRepository productRepository;

    private final EventProducer eventProducer;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductConsumer.class);

    @KafkaListener(topics = "order-created", groupId = "product-group")
    @Transactional
    public void consumeOrderCreated(OrderEvent event) {
        LOGGER.info("[OrderLineItemConsumer] Received OrderCreated Event: orderId={}", event.orderId());

        Map<Long, Integer> reservedStocks = reserveStock(event);

        // reservedStocks.size(): number of products whose stock was successfully reserved
        // event.orderLineItems().size(): total number of products in the order
        boolean fullyReserved = reservedStocks.size() == event.orderLineItems().size();

        if (fullyReserved) {
            StockReservedEvent successEvent = new StockReservedEvent(
                event.orderId(), event.userId(), event.amount(), event.orderLineItems(), reservedStocks);

            eventProducer.sendMessage("stock-reserved", successEvent);
        } else {
            reservedStocks.forEach(productRepository::rollbackStock);
            InsufficientStockEvent failedEvent = new InsufficientStockEvent(
                event.orderId(), event.userId(), event.amount(), event.paymentMethod());
            eventProducer.sendMessage("stock-reservation-failed", failedEvent);
        }
    }

    @KafkaListener(topics = "payment-failed", groupId = "product-group")
    @Transactional
    public void consumePaymentFailed(PaymentFailedEvent event) {
        LOGGER.info("[OrderLineItemConsumer] Received PaymentFailed Event: orderId={}", event.orderId());

        event.reservedStocks().forEach(productRepository::rollbackStock);
    }

    // Decreases the product stock in the database if enough stock is available
    // and returns a map of product IDs to the quantities that were successfully reserved
    private Map<Long, Integer> reserveStock(OrderEvent event) {
        Map<Long, Integer> reservedStocks = new HashMap<>();

        for (var item : event.orderLineItems()) {
            int updated = productRepository.decreaseStockIfAvailable(item.getProductId(), item.getQuantity());
            if (updated == 0) break;
            reservedStocks.put(item.getProductId(), item.getQuantity());
        }

        return reservedStocks;
    }
}
