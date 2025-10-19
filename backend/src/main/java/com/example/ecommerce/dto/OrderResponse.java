package com.example.ecommerce.dto;

import java.math.BigInteger;
import java.util.List;

import com.example.ecommerce.model.OrderStatus;
import com.example.ecommerce.model.PaymentMethod;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    
    private Long id;

    private BigInteger amount;

    private OrderStatus status;

    private List<OrderLineItemResponse> orderLineItems;

    private PaymentMethod paymentMethod;
}
