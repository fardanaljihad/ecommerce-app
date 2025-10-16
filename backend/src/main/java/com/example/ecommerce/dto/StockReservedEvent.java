package com.example.ecommerce.dto;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public record StockReservedEvent(
    Long orderId, Long userId, BigInteger amount, List<CreateOrderLineItemRequest> orderLineItems, Map<Long, Integer> reservedStocks) {
    
}
