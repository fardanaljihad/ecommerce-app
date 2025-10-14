package com.example.ecommerce.service;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.ecommerce.dto.CreateOrderRequest;
import com.example.ecommerce.dto.CreateOrderResponse;
import com.example.ecommerce.dto.OrderEvent;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.OrderStatus;
import com.example.ecommerce.model.PaymentMethod;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ValidationService validationService;

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;
    
    @Transactional
    public CreateOrderResponse create(CreateOrderRequest request) {
        validationService.validate(request);

        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setAmount(request.getAmount());
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(new Date());

        Order saved = orderRepository.save(order);

        OrderEvent event = new OrderEvent(
            saved.getId(), 
            saved.getAmount(), 
            saved.getStatus(), 
            saved.getUser().getId(), 
            request.getOrderLineItems(), 
            saved.getCreatedAt(), 
            PaymentMethod.valueOf(request.getPaymentMethod()));

        kafkaTemplate.send("order-created", event);

        return CreateOrderResponse.builder()
            .id(saved.getId())
            .status(saved.getStatus().name())
            .build();
    }
}
