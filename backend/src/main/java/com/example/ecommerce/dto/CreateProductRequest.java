package com.example.ecommerce.dto;

import java.math.BigInteger;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {
    
    @NotBlank
    @Size(min = 3, max = 100)
    private String name;

    @NotNull
    private BigInteger price;

    @NotNull
    private Integer stock;
}
