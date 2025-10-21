package com.example.ecommerce.dto;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderLineItemResponse {
    
    private Long id;

    private String name;

    private Integer quantity;

    private BigInteger price;
}
