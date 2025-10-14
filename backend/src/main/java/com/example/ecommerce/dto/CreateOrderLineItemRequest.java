package com.example.ecommerce.dto;

import java.math.BigInteger;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderLineItemRequest {

    @NotNull
    private Long orderId;

    @NotNull
    private Long productId;

    @NotNull
    private Integer quantity;

    @NotNull
    private BigInteger price;
}
