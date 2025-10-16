package com.example.ecommerce.dto;

import java.math.BigInteger;
import java.util.List;

public record StockReservedEvent(Long orderId, Long userId, BigInteger amount, List<CreateOrderLineItemRequest> orderLineItems) {
    
}
