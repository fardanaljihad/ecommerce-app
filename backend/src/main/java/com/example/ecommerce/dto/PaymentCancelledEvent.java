package com.example.ecommerce.dto;

public record PaymentCancelledEvent(Long paymentId, Long orderId, String reason) {
    
}
