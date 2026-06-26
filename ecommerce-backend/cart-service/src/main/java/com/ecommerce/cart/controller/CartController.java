package com.ecommerce.cart.controller;

import com.ecommerce.cart.dto.AddToCartRequest;
import com.ecommerce.cart.dto.CartDTO;
import com.ecommerce.cart.service.CartService;
import com.ecommerce.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@Tag(name = "Cart Management", description = "Shopping cart endpoints")
@Slf4j
public class CartController {
    
    private final CartService cartService;
    
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    
    @PostMapping("/{userId}/items")
    @Operation(summary = "Add item to cart")
    public ResponseEntity<ApiResponse<CartDTO>> addToCart(@PathVariable Long userId,
                                                         @RequestBody AddToCartRequest request) {
        log.info("Adding product {} to cart for user {}", request.getProductId(), userId);
        
        CartDTO cartDTO = cartService.addToCart(userId, request.getProductId(), request.getQuantity());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(cartDTO, "Item added to cart"));
    }
    
    @PutMapping("/{userId}/items/{productId}")
    @Operation(summary = "Update cart item quantity")
    public ResponseEntity<ApiResponse<CartDTO>> updateCartItem(@PathVariable Long userId,
                                                              @PathVariable Long productId,
                                                              @RequestParam Integer quantity) {
        log.info("Updating product {} quantity to {} in cart for user {}", productId, quantity, userId);
        
        CartDTO cartDTO = cartService.updateCartItem(userId, productId, quantity);
        return ResponseEntity.ok(ApiResponse.success(cartDTO, "Cart item updated"));
    }
    
    @DeleteMapping("/{userId}/items/{productId}")
    @Operation(summary = "Remove item from cart")
    public ResponseEntity<ApiResponse<CartDTO>> removeFromCart(@PathVariable Long userId,
                                                              @PathVariable Long productId) {
        log.info("Removing product {} from cart for user {}", productId, userId);
        
        CartDTO cartDTO = cartService.removeFromCart(userId, productId);
        return ResponseEntity.ok(ApiResponse.success(cartDTO, "Item removed from cart"));
    }
    
    @GetMapping("/{userId}")
    @Operation(summary = "Get cart for user")
    public ResponseEntity<ApiResponse<CartDTO>> getCart(@PathVariable Long userId) {
        log.info("Fetching cart for user {}", userId);
        
        CartDTO cartDTO = cartService.getCart(userId);
        return ResponseEntity.ok(ApiResponse.success(cartDTO, "Cart retrieved successfully"));
    }
    
    @DeleteMapping("/{userId}/clear")
    @Operation(summary = "Clear all items from cart")
    public ResponseEntity<ApiResponse<String>> clearCart(@PathVariable Long userId) {
        log.info("Clearing cart for user {}", userId);
        
        cartService.clearCart(userId);
        return ResponseEntity.ok(ApiResponse.success(null, "Cart cleared"));
    }
}
