package com.example.ecommerce.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.dto.ApiResponse;
import com.example.ecommerce.dto.RegisterUserRequest;
import com.example.ecommerce.service.UserService;
import com.example.ecommerce.service.ValidationService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    private final ValidationService validationService;
    
    @PostMapping("/users/register")
    public ApiResponse<String> register(@RequestBody RegisterUserRequest request) {

        validationService.validate(request);
        userService.register(request);
        return ApiResponse.<String>builder().data("OK").build();
    }
}
