package com.example.ecommerce.dto;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.example.ecommerce.model.OrderStatus;
import com.example.ecommerce.model.PaymentMethod;


public record OrderEvent(
    Long orderId, 
    BigInteger amount, 
    OrderStatus status, 
    Long userId, 
    List<CreateOrderLineItemRequest> orderLineItems, 
    Date createdAt,
    PaymentMethod paymentMethod
) {}
