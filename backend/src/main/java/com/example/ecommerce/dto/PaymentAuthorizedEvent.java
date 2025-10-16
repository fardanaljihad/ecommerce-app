package com.example.ecommerce.dto;

import java.math.BigInteger;
import java.util.List;

public record PaymentAuthorizedEvent(Long paymentId, Long orderId, BigInteger amount, List<CreateOrderLineItemRequest> orderLineItemRequests) {
    
}
