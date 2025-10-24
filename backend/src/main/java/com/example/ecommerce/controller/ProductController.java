package com.example.ecommerce.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.dto.ApiResponse;
import com.example.ecommerce.dto.CreateProductRequest;
import com.example.ecommerce.dto.PaginationResponse;
import com.example.ecommerce.dto.ProductResponse;
import com.example.ecommerce.dto.SearchProductRequest;
import com.example.ecommerce.dto.UpdateProductRequest;
import com.example.ecommerce.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {

    private final ProductService productService;
    
    @PostMapping("/products")
    public ApiResponse<String> create(@RequestBody CreateProductRequest request) {
        productService.create(request);
        return ApiResponse.<String>builder().data("OK").build();
    }

    @GetMapping("/products/{id}")
    public ApiResponse<ProductResponse> get(@PathVariable Long id) {
        ProductResponse productResponse = productService.get(id);
        return ApiResponse.<ProductResponse>builder().data(productResponse).build();
    }
    
    @GetMapping("/products")
    public ApiResponse<List<ProductResponse>> search(
        @RequestParam(required = false) String name,
        @RequestParam(required = false, defaultValue = "0") Integer page,
        @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        SearchProductRequest request = SearchProductRequest.builder()
            .name(name)
            .page(page)
            .size(size)
            .build();

        Page<ProductResponse> productResponses = productService.search(request);

        return ApiResponse.<List<ProductResponse>>builder()
            .data(productResponses.getContent())
            .pagination(PaginationResponse.builder()
                .currentPage(productResponses.getNumber())
                .totalPage(productResponses.getTotalPages())
                .size(productResponses.getSize())
                .build()
            )
            .build();
    }
    
    @PutMapping("/products/{productId}")
    public ApiResponse<ProductResponse> update(
        @RequestBody UpdateProductRequest request,
        @PathVariable Long productId
    ) {
        request.setId(productId);

        ProductResponse productResponse = productService.update(request);

        return ApiResponse.<ProductResponse>builder().data(productResponse).build();
    }

    @DeleteMapping("/products/{productId}")
    public ApiResponse<String> delete(@PathVariable Long productId) {
        productService.delete(productId);

        return ApiResponse.<String>builder().data("OK").build();
    }
}
