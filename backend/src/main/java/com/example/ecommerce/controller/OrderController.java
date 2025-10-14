package com.example.ecommerce.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.dto.ApiResponse;
import com.example.ecommerce.dto.CreateOrderRequest;
import com.example.ecommerce.dto.CreateOrderResponse;
import com.example.ecommerce.service.OrderService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {
    
    private final OrderService orderService;

    @PostMapping("/orders")
    public ApiResponse<CreateOrderResponse> create(@RequestBody CreateOrderRequest request) {
        CreateOrderResponse response = orderService.create(request);
        return ApiResponse.<CreateOrderResponse>builder().data(response).build();
    }
    
}
