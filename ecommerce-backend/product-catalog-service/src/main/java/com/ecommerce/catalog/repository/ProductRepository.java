package com.ecommerce.catalog.repository;

import com.ecommerce.catalog.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySku(String sku);
    
    Page<Product> findByStatus(String status, Pageable pageable);
    
    Page<Product> findByCategory(Long categoryId, Pageable pageable);
    
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.status = 'ACTIVE' AND p.stockQuantity < :threshold")
    Page<Product> findLowStockProducts(@Param("threshold") Integer threshold, Pageable pageable);
}
