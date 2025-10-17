package com.example.ecommerce.dto;

import java.math.BigInteger;
import java.util.Map;

import com.example.ecommerce.model.PaymentMethod;

public record PaymentFailedEvent(Long paymentId, Long orderId, BigInteger amount, PaymentMethod paymentMethod, Map<Long, Integer> reservedStocks, String reason) {
    
}
