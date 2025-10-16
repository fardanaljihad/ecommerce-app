package com.example.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ecommerce.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Modifying
    @Query("""
        UPDATE Product p
        SET p.stock = p.stock - :quantity, p.updatedAt = CURRENT_TIMESTAMP
        WHERE p.id = :productId AND p.stock >= :quantity
    """)
    int decreaseStockIfAvailable(@Param("productId") Long productId, @Param("quantity") Integer quantity);

    @Modifying
    @Query("""
        UPDATE Product p
        SET p.stock = p.stock + :quantity, p.updatedAt = CURRENT_TIMESTAMP
        WHERE p.id = :productId
    """)
    int rollbackStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);
}
