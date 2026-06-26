package com.ecommerce.cart.client;

import com.ecommerce.shared.dto.ApiResponse;
import com.ecommerce.shared.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-catalog-service", url = "http://localhost:8002")
public interface ProductServiceClient {
    
    @GetMapping("/api/products/{id}")
    ApiResponse<ProductDTO> getProductById(@PathVariable Long id);
    
    @GetMapping("/api/products/sku/{sku}")
    ApiResponse<ProductDTO> getProductBySku(@PathVariable String sku);
}
