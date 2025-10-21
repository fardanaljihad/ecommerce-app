package com.example.ecommerce.service;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.ecommerce.dto.CreateOrderRequest;
import com.example.ecommerce.dto.CreateOrderResponse;
import com.example.ecommerce.dto.OrderEvent;
import com.example.ecommerce.dto.OrderLineItemResponse;
import com.example.ecommerce.dto.OrderResponse;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.OrderStatus;
import com.example.ecommerce.model.PaymentMethod;
import com.example.ecommerce.model.User;
import com.example.ecommerce.producer.EventProducer;
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

    private final EventProducer eventProducer;
    
    @Transactional
    public CreateOrderResponse create(CreateOrderRequest request) {
        validationService.validate(request);

        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setAmount(request.getAmount());
        order.setStatus(OrderStatus.PENDING_APPROVAL);
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

        eventProducer.sendMessage("order-created", event);

        return CreateOrderResponse.builder()
            .id(saved.getId())
            .status(saved.getStatus().name())
            .build();
    }

    @Transactional
    public OrderResponse get(Long id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        return toOrderResponse(order);
    }

    private OrderResponse toOrderResponse(Order order) {
        List<OrderLineItemResponse> orderLineItems = order.getOrderLineItems().stream()
            .map(item -> new OrderLineItemResponse(item.getId(), item.getProduct().getName(), item.getQuantity(), item.getPrice()))
            .toList();

        return OrderResponse.builder()
            .id(order.getId())
            .amount(order.getAmount())
            .status(order.getStatus())
            .orderLineItems(orderLineItems)
            .paymentMethod(order.getPayment().getPaymentMethod())
            .createdAt(order.getCreatedAt())
            .updatedAt(order.getUpdatedAt())
            .build();
    }
}
