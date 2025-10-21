package com.example.ecommerce.consumer;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.example.ecommerce.dto.OrderEvent;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.OrderLineItem;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.OrderLineItemRepository;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderLineItemConsumer {
    
    private final OrderRepository orderRepository;

    private final OrderLineItemRepository orderLineItemRepository;

    private final ProductRepository productRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderLineItemConsumer.class);
    
    @KafkaListener(topics = "order-created", groupId = "order-line-item-group")
    @Transactional
    public void consumeOrderCreated(OrderEvent event) {
        LOGGER.info("[OrderLineItemConsumer] Received OrderCreated Event: orderId={}", event.orderId());

        Order order = orderRepository.findById(event.orderId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        
        List<OrderLineItem> orderLineItems = event.orderLineItems().stream()
            .map(item -> {

                Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

                OrderLineItem orderLineItem = new OrderLineItem();
                orderLineItem.setOrder(order);
                orderLineItem.setProduct(product);
                orderLineItem.setQuantity(item.getQuantity());
                orderLineItem.setPrice(item.getPrice());
                orderLineItem.setCreatedAt(new Date());

                return orderLineItem;
            })
            .toList();

        orderLineItemRepository.saveAll(orderLineItems);
    }
}
