package com.example.ecommerce.service;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.ecommerce.dto.LoginUserRequest;
import com.example.ecommerce.dto.TokenResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final ValidationService validationService;

    private final JwtService jwtService;
    
    public TokenResponse login(LoginUserRequest request) {

        validationService.validate(request);

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        List<String> roles = authentication.getAuthorities()
            .stream()
            .map(grantedAuthority -> grantedAuthority.getAuthority())
            .toList();

        String token = jwtService.generateToken(request.getUsername(), roles);

        return TokenResponse.builder().token(token).build();
    }
}
