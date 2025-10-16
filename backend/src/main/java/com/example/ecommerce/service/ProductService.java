package com.example.ecommerce.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.ecommerce.dto.CreateProductRequest;
import com.example.ecommerce.dto.InsufficientStockEvent;
import com.example.ecommerce.dto.OrderEvent;
import com.example.ecommerce.dto.ProductResponse;
import com.example.ecommerce.dto.SearchProductRequest;
import com.example.ecommerce.dto.StockReservedEvent;
import com.example.ecommerce.dto.UpdateProductRequest;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;

    private final ValidationService validationService;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "order-created", groupId = "product-group")
    @Transactional
    public void consume(OrderEvent event) {

        Map<Long, Integer> reservedStocks = reserveStock(event);

        // reservedStocks.size(): number of products whose stock was successfully reserved
        // event.orderLineItems().size(): total number of products in the order
        boolean fullyReserved = reservedStocks.size() == event.orderLineItems().size();

        if (fullyReserved) {
            StockReservedEvent successEvent = new StockReservedEvent(
                event.orderId(), event.userId(), event.amount(), event.orderLineItems(), reservedStocks);

            kafkaTemplate.send("stock-reserved", successEvent);
        } else {
            reservedStocks.forEach(productRepository::rollbackStock);
            InsufficientStockEvent failedEvent = new InsufficientStockEvent(event.orderId(), event.userId());
            kafkaTemplate.send("stock-reservation-failed", failedEvent);
        }
    }

    // Decreases the product stock in the database if enough stock is available
    // and returns a map of product IDs to the quantities that were successfully reserved
    private Map<Long, Integer> reserveStock(OrderEvent event) {
        Map<Long, Integer> reservedStocks = new HashMap<>();

        for (var item : event.orderLineItems()) {
            int updated = productRepository.decreaseStockIfAvailable(item.getProductId(), item.getQuantity());
            if (updated == 0) break;
            reservedStocks.put(item.getProductId(), item.getQuantity());
        }

        return reservedStocks;
    }

    public void create(CreateProductRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        validationService.authorize(authentication.getAuthorities(), List.of("OWNER"));
        validationService.validate(request);

        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCreatedAt(new Date());

        productRepository.save(product);
    }

    public ProductResponse get(Long id) {
        Product product = productRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        return toProductResponse(product);
    }

    public Page<ProductResponse> search(SearchProductRequest request) {
        Specification<Product> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (Objects.nonNull(request.getName())) {
                predicates.add(builder.like(root.get("name"), "%" + request.getName() + "%"));
            }

            return query.where(predicates.toArray(new Predicate[0])).getRestriction();
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        Page<Product> products = productRepository.findAll(specification, pageable);
        
        List<ProductResponse> productResponses = products.getContent()
                .stream()
                .map(this::toProductResponse)
                .toList();

        return new PageImpl<>(productResponses, pageable, products.getTotalElements());
    }

    @Transactional
    public ProductResponse update(UpdateProductRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        validationService.authorize(authentication.getAuthorities(), List.of("OWNER"));
        validationService.validate(request);

        Product product = productRepository.findById(request.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setUpdatedAt(new Date());

        productRepository.save(product);

        return toProductResponse(product);
    }

    @Transactional
    public void delete(Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        validationService.authorize(authentication.getAuthorities(), List.of("OWNER"));

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        productRepository.delete(product);
    }

    private ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .price(product.getPrice())
            .stock(product.getStock())
            .build();
    }
}
