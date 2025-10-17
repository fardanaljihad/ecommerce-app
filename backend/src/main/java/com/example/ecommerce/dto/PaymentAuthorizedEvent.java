package com.example.ecommerce.dto;

import java.math.BigInteger;
import java.util.List;

import com.example.ecommerce.model.PaymentMethod;

public record PaymentAuthorizedEvent(Long paymentId, Long orderId, BigInteger amount, PaymentMethod paymentMethod, List<CreateOrderLineItemRequest> orderLineItemRequests) {
    
}
