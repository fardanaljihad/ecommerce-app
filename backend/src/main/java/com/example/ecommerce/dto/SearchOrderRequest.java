package com.example.ecommerce.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchOrderRequest {
    
    private String username;

    @NotNull
    private Integer page;

    @NotNull
    private Integer size;
}
