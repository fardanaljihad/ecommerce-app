package com.example.ecommerce.dto;

import java.math.BigInteger;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProductRequest {

    @NotNull
    private Long id;
    
    @Size(min = 3, max = 100)
    private String name;

    @NotNull
    private BigInteger price;

    @NotNull
    private Integer stock;
}
