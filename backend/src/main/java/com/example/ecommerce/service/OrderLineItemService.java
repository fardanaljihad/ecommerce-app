package com.example.ecommerce.service;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
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

@Service
@RequiredArgsConstructor
public class OrderLineItemService {

    private final OrderRepository orderRepository;

    private final OrderLineItemRepository orderLineItemRepository;

    private final ProductRepository productRepository;
    
    @KafkaListener(topics = "order-created", groupId = "order-line-item-group")
    @Transactional
    public void consume(OrderEvent event) {

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
