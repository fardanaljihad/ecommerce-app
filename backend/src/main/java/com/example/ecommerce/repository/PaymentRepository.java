package com.example.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommerce.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    Optional<Payment> findByOrder_Id(Long orderId);
}
