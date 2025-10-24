package com.example.ecommerce.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.dto.ApiResponse;
import com.example.ecommerce.dto.CreateOrderRequest;
import com.example.ecommerce.dto.CreateOrderResponse;
import com.example.ecommerce.dto.OrderResponse;
import com.example.ecommerce.dto.PaginationResponse;
import com.example.ecommerce.dto.SearchOrderRequest;
import com.example.ecommerce.service.OrderService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {
    
    private final OrderService orderService;

    @PostMapping("/orders")
    public ApiResponse<CreateOrderResponse> create(@RequestBody CreateOrderRequest request) {
        CreateOrderResponse response = orderService.create(request);
        return ApiResponse.<CreateOrderResponse>builder().data(response).build();
    }

    @GetMapping("/orders/{id}")
    public ApiResponse<OrderResponse> get(@PathVariable Long id) {
        OrderResponse response = orderService.get(id);
        return ApiResponse.<OrderResponse>builder().data(response).build();
    }

    @GetMapping("/orders")
    public ApiResponse<List<OrderResponse>> search(
        @RequestParam(required = false) Long userId,
        @RequestParam(required = false, defaultValue = "0") Integer page,
        @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        SearchOrderRequest request = SearchOrderRequest.builder()
            .userId(userId)
            .page(page)
            .size(size)
            .build();

        Page<OrderResponse> orderResponses = orderService.search(request);

        return ApiResponse.<List<OrderResponse>>builder()
            .data(orderResponses.getContent())
            .pagination(PaginationResponse.builder()
                .currentPage(orderResponses.getNumber())
                .totalPage(orderResponses.getTotalPages())
                .size(orderResponses.getSize())
                .build()
            )
            .build();
    }
}
