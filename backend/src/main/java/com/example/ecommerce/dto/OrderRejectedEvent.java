package com.example.ecommerce.dto;

import java.math.BigInteger;

import com.example.ecommerce.model.PaymentMethod;

public record OrderRejectedEvent(Long orderId, Long userId, BigInteger amount, PaymentMethod paymentMethod, String reason) {
    
}
