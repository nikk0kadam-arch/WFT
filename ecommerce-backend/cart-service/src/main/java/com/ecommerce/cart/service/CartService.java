package com.ecommerce.cart.service;

import com.ecommerce.cart.client.ProductServiceClient;
import com.ecommerce.cart.dto.CartDTO;
import com.ecommerce.cart.dto.CartItemDTO;
import com.ecommerce.cart.entity.Cart;
import com.ecommerce.cart.entity.CartItem;
import com.ecommerce.cart.repository.CartItemRepository;
import com.ecommerce.cart.repository.CartRepository;
import com.ecommerce.shared.dto.ProductDTO;
import com.ecommerce.shared.exception.BusinessException;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class CartService {
    
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductServiceClient productServiceClient;
    
    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
                      ProductServiceClient productServiceClient) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productServiceClient = productServiceClient;
    }
    
    public Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUserIdAndStatus(userId, "ACTIVE")
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUserId(userId);
                    cart.setStatus("ACTIVE");
                    return cartRepository.save(cart);
                });
    }
    
    public CartDTO addToCart(Long userId, Long productId, Integer quantity) {
        if (quantity <= 0) {
            throw new BusinessException("Quantity must be greater than 0", "INVALID_QUANTITY", 400);
        }
        
        // Get product details from Product Service
        ProductDTO productDTO = productServiceClient.getProductById(productId).getData();
        
        if (productDTO == null) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
        
        Cart cart = getOrCreateCart(userId);
        
        // Check if item already exists
        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId);
        
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            item.calculateTotal();
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProductId(productId);
            newItem.setQuantity(quantity);
            newItem.setUnitPrice(BigDecimal.valueOf(productDTO.getPrice()));
            newItem.calculateTotal();
            cartItemRepository.save(newItem);
        }
        
        cart.calculateTotal();
        cartRepository.save(cart);
        
        return convertToDTO(cart);
    }
    
    public CartDTO updateCartItem(Long userId, Long productId, Integer quantity) {
        if (quantity < 0) {
            throw new BusinessException("Quantity cannot be negative", "INVALID_QUANTITY", 400);
        }
        
        Cart cart = getOrCreateCart(userId);
        CartItem item = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in cart"));
        
        if (quantity == 0) {
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
            item.calculateTotal();
            cartItemRepository.save(item);
        }
        
        cart.calculateTotal();
        cartRepository.save(cart);
        
        return convertToDTO(cart);
    }
    
    public CartDTO removeFromCart(Long userId, Long productId) {
        Cart cart = getOrCreateCart(userId);
        CartItem item = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in cart"));
        
        cartItemRepository.delete(item);
        cart.calculateTotal();
        cartRepository.save(cart);
        
        return convertToDTO(cart);
    }
    
    public CartDTO getCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        return convertToDTO(cart);
    }
    
    public void clearCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        cart.getItems().clear();
        cart.setTotalAmount(BigDecimal.ZERO);
        cartRepository.save(cart);
    }
    
    private CartDTO convertToDTO(Cart cart) {
        return new CartDTO(
                cart.getId(),
                cart.getUserId(),
                cart.getItems().stream()
                        .map(item -> new CartItemDTO(
                                item.getId(),
                                item.getProductId(),
                                item.getQuantity(),
                                item.getUnitPrice(),
                                item.getTotalPrice()
                        ))
                        .collect(Collectors.toList()),
                cart.getTotalAmount(),
                cart.getStatus()
        );
    }
}
