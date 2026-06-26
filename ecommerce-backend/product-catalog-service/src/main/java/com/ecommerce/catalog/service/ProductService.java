package com.ecommerce.catalog.service;

import com.ecommerce.catalog.entity.Category;
import com.ecommerce.catalog.entity.Product;
import com.ecommerce.catalog.repository.CategoryRepository;
import com.ecommerce.catalog.repository.ProductRepository;
import com.ecommerce.shared.dto.ProductDTO;
import com.ecommerce.shared.exception.BusinessException;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Slf4j
@Transactional
public class ProductService {
    
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }
    
    @CacheEvict(value = "products", allEntries = true)
    public Product createProduct(String name, String description, BigDecimal price, 
                                BigDecimal cost, String sku, Long categoryId, Integer stockQuantity) {
        if (productRepository.findBySku(sku).isPresent()) {
            throw new BusinessException("Product already exists with SKU: " + sku, "SKU_EXISTS", 400);
        }
        
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCost(cost);
        product.setSku(sku);
        product.setStockQuantity(stockQuantity);
        product.setStatus("ACTIVE");
        
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
            product.setCategory(category);
        }
        
        return productRepository.save(product);
    }
    
    @Cacheable(value = "products", key = "#id")
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }
    
    @Cacheable(value = "products", key = "#sku")
    public Product getProductBySku(String sku) {
        return productRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with SKU: " + sku));
    }
    
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findByStatus("ACTIVE", pageable);
    }
    
    public Page<Product> searchProducts(String name, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCase(name, pageable);
    }
    
    public Page<Product> getProductsByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findByCategory(categoryId, pageable);
    }
    
    @CacheEvict(value = "products", key = "#id")
    public Product updateProduct(Long id, String name, String description, BigDecimal price,
                                BigDecimal cost, Integer stockQuantity) {
        Product product = getProductById(id);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCost(cost);
        product.setStockQuantity(stockQuantity);
        
        return productRepository.save(product);
    }
    
    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        product.setStatus("INACTIVE");
        productRepository.save(product);
    }
    
    public Page<Product> getLowStockProducts(Integer threshold, Pageable pageable) {
        return productRepository.findLowStockProducts(threshold, pageable);
    }
    
    @CacheEvict(value = "products", key = "#id")
    public void updateStock(Long productId, Integer quantity) {
        Product product = getProductById(productId);
        int newStock = product.getStockQuantity() + quantity;
        
        if (newStock < 0) {
            throw new BusinessException("Insufficient stock available", "INSUFFICIENT_STOCK", 400);
        }
        
        product.setStockQuantity(newStock);
        productRepository.save(product);
    }
    
    public ProductDTO convertToDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice().doubleValue(),
                product.getSku(),
                product.getCategory() != null ? product.getCategory().getId() : null,
                product.getStockQuantity(),
                product.getStatus()
        );
    }
}
