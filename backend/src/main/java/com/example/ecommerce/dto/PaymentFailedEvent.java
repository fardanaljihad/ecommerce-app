package com.example.ecommerce.dto;

import java.util.Map;

public record PaymentFailedEvent(Long paymentId, Long orderId, Map<Long, Integer> reservedStocks, String reason) {
    
}
