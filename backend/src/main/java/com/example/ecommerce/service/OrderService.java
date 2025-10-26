package com.example.ecommerce.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.ecommerce.dto.CreateOrderRequest;
import com.example.ecommerce.dto.CreateOrderResponse;
import com.example.ecommerce.dto.OrderEvent;
import com.example.ecommerce.dto.OrderLineItemResponse;
import com.example.ecommerce.dto.OrderResponse;
import com.example.ecommerce.dto.SearchOrderRequest;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.OrderStatus;
import com.example.ecommerce.model.PaymentMethod;
import com.example.ecommerce.model.User;
import com.example.ecommerce.producer.EventProducer;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.UserRepository;

import jakarta.persistence.criteria.Predicate;
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

        User user = userRepository.findByUsername(request.getUsername())
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

    public OrderResponse get(Long id) {
        Order order = orderRepository.findByIdWithDetails(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        return toOrderResponse(order);
    }

    public Page<OrderResponse> search(SearchOrderRequest request) {
        Specification<Order> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(request.getUsername())) {
                predicates.add(builder.equal(root.get("user").get("username"), request.getUsername()));
            }

            query.orderBy(builder.desc(root.get("createdAt")));

            return query.where(predicates.toArray(new Predicate[0])).getRestriction();
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        Page<Order> orders = orderRepository.findAll(specification, pageable);

        List<OrderResponse> orderResponses = orders.getContent()
            .stream()
            .map(this::toOrderResponse)
            .toList();

        return new PageImpl<>(orderResponses, pageable, orders.getTotalElements());
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
