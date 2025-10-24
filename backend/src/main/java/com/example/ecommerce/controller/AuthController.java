package com.example.ecommerce.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.dto.ApiResponse;
import com.example.ecommerce.dto.LoginUserRequest;
import com.example.ecommerce.dto.TokenResponse;
import com.example.ecommerce.service.AuthService;
import com.example.ecommerce.service.ValidationService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final ValidationService validationService;
    
    private final AuthService authService;

    @PostMapping("/auth/login")
    public ApiResponse<TokenResponse> login(@RequestBody LoginUserRequest request) {
        validationService.validate(request);
        TokenResponse response = authService.login(request);
        return ApiResponse.<TokenResponse>builder().data(response).build();
    }

    @GetMapping("/ping")
    public String ping() {
        return "OK";
    }
}
