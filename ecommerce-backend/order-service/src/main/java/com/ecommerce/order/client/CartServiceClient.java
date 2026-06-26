package com.ecommerce.order.client;

import com.ecommerce.shared.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cart-service", url = "http://localhost:8003")
public interface CartServiceClient {
    
    @GetMapping("/api/carts/{userId}")
    ApiResponse<?> getCart(@PathVariable Long userId);
    
    @DeleteMapping("/api/carts/{userId}/clear")
    ApiResponse<?> clearCart(@PathVariable Long userId);
}
