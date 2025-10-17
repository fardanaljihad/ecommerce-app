package com.example.ecommerce.dto;

import java.math.BigInteger;

import com.example.ecommerce.model.PaymentMethod;

public record OrderApprovedEvent(Long orderId, Long userId, BigInteger amount, PaymentMethod paymentMethod) {
    
}
