package com.example.ecommerce.dto;

import java.math.BigInteger;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {

    @NotNull
    private Long userId;
    
    @NotNull
    private BigInteger amount;

    @NotNull
    private List<CreateOrderLineItemRequest> orderLineItems;

    @NotBlank
    private String paymentMethod;
}
