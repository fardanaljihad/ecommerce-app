package com.example.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {
        "orderLineItems",
        "orderLineItems.product",
        "payment"
    })
    @Query("SELECT o FROM Order o WHERE o.id = :id")
    Optional<Order> findByIdWithDetails(@Param("id") Long id);
}
