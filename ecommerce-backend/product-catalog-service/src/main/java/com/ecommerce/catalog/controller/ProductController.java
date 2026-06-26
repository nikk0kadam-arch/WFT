package com.ecommerce.catalog.controller;

import com.ecommerce.catalog.dto.CreateProductRequest;
import com.ecommerce.catalog.entity.Product;
import com.ecommerce.catalog.service.ProductService;
import com.ecommerce.shared.dto.ApiResponse;
import com.ecommerce.shared.dto.ProductDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product Management", description = "Product catalog endpoints")
@Slf4j
public class ProductController {
    
    private final ProductService productService;
    
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    @PostMapping
    @Operation(summary = "Create a new product")
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(@RequestBody CreateProductRequest request) {
        log.info("Creating product: {}", request.getName());
        
        Product product = productService.createProduct(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getCost(),
                request.getSku(),
                request.getCategoryId(),
                request.getStockQuantity()
        );
        
        ProductDTO productDTO = productService.convertToDTO(product);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(productDTO, "Product created successfully"));
    }
    
    @GetMapping
    @Operation(summary = "Get all active products")
    public ResponseEntity<ApiResponse<Page<ProductDTO>>> getAllProducts(Pageable pageable) {
        log.info("Fetching all products");
        
        Page<Product> products = productService.getAllProducts(pageable);
        Page<ProductDTO> productDTOs = products.map(productService::convertToDTO);
        return ResponseEntity.ok(ApiResponse.success(productDTOs, "Products retrieved successfully"));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductById(@PathVariable Long id) {
        log.info("Fetching product: {}", id);
        
        Product product = productService.getProductById(id);
        ProductDTO productDTO = productService.convertToDTO(product);
        return ResponseEntity.ok(ApiResponse.success(productDTO, "Product retrieved successfully"));
    }
    
    @GetMapping("/sku/{sku}")
    @Operation(summary = "Get product by SKU")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductBySku(@PathVariable String sku) {
        log.info("Fetching product by SKU: {}", sku);
        
        Product product = productService.getProductBySku(sku);
        ProductDTO productDTO = productService.convertToDTO(product);
        return ResponseEntity.ok(ApiResponse.success(productDTO, "Product retrieved successfully"));
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search products by name")
    public ResponseEntity<ApiResponse<Page<ProductDTO>>> searchProducts(@RequestParam String name, Pageable pageable) {
        log.info("Searching products: {}", name);
        
        Page<Product> products = productService.searchProducts(name, pageable);
        Page<ProductDTO> productDTOs = products.map(productService::convertToDTO);
        return ResponseEntity.ok(ApiResponse.success(productDTOs, "Products found"));
    }
    
    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get products by category")
    public ResponseEntity<ApiResponse<Page<ProductDTO>>> getProductsByCategory(@PathVariable Long categoryId,
                                                                               Pageable pageable) {
        log.info("Fetching products by category: {}", categoryId);
        
        Page<Product> products = productService.getProductsByCategory(categoryId, pageable);
        Page<ProductDTO> productDTOs = products.map(productService::convertToDTO);
        return ResponseEntity.ok(ApiResponse.success(productDTOs, "Products retrieved successfully"));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update product")
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(@PathVariable Long id,
                                                                @RequestBody CreateProductRequest request) {
        log.info("Updating product: {}", id);
        
        Product product = productService.updateProduct(
                id,
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getCost(),
                request.getStockQuantity()
        );
        
        ProductDTO productDTO = productService.convertToDTO(product);
        return ResponseEntity.ok(ApiResponse.success(productDTO, "Product updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable Long id) {
        log.info("Deleting product: {}", id);
        
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Product deleted successfully"));
    }
    
    @GetMapping("/low-stock")
    @Operation(summary = "Get low stock products")
    public ResponseEntity<ApiResponse<Page<ProductDTO>>> getLowStockProducts(
            @RequestParam(defaultValue = "10") Integer threshold,
            Pageable pageable) {
        log.info("Fetching low stock products with threshold: {}", threshold);
        
        Page<Product> products = productService.getLowStockProducts(threshold, pageable);
        Page<ProductDTO> productDTOs = products.map(productService::convertToDTO);
        return ResponseEntity.ok(ApiResponse.success(productDTOs, "Low stock products retrieved"));
    }
}
