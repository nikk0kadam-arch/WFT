package com.ecommerce.order.service;

import com.ecommerce.order.client.CartServiceClient;
import com.ecommerce.order.client.ProductServiceClient;
import com.ecommerce.order.dto.OrderDTO;
import com.ecommerce.order.dto.OrderItemDTO;
import com.ecommerce.order.entity.Order;
import com.ecommerce.order.entity.OrderItem;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.shared.dto.ApiResponse;
import com.ecommerce.shared.dto.CartDTO;
import com.ecommerce.shared.dto.CartItemDTO;
import com.ecommerce.shared.dto.ProductDTO;
import com.ecommerce.shared.exception.BusinessException;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final CartServiceClient cartServiceClient;
    private final ProductServiceClient productServiceClient;
    
    public OrderService(OrderRepository orderRepository, CartServiceClient cartServiceClient,
                       ProductServiceClient productServiceClient) {
        this.orderRepository = orderRepository;
        this.cartServiceClient = cartServiceClient;
        this.productServiceClient = productServiceClient;
    }
    
    public OrderDTO createOrder(Long userId, String shippingAddress, String billingAddress) {
        log.info("Creating order for user: {}", userId);
        
        // Get cart from Cart Service
        ApiResponse<CartDTO> cartResponse = (ApiResponse<CartDTO>) cartServiceClient.getCart(userId);
        CartDTO cartDTO = cartResponse.getData();
        
        if (cartDTO == null || cartDTO.getItems().isEmpty()) {
            throw new BusinessException("Cart is empty", "EMPTY_CART", 400);
        }
        
        // Create order from cart items
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderNumber(generateOrderNumber());
        order.setShippingAddress(shippingAddress);
        order.setBillingAddress(billingAddress);
        order.setStatus("PENDING");
        
        BigDecimal totalAmount = BigDecimal.ZERO;
        
        for (CartItemDTO cartItem : cartDTO.getItems()) {
            // Get product details
            ApiResponse<ProductDTO> productResponse = productServiceClient.getProductById(cartItem.getProductId());
            ProductDTO productDTO = productResponse.getData();
            
            if (productDTO == null) {
                throw new ResourceNotFoundException("Product not found: " + cartItem.getProductId());
            }
            
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setProductName(productDTO.getName());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(cartItem.getUnitPrice());
            orderItem.setTotalPrice(cartItem.getTotalPrice());
            
            order.getItems().add(orderItem);
            totalAmount = totalAmount.add(cartItem.getTotalPrice());
        }
        
        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);
        
        // Clear cart after order creation
        cartServiceClient.clearCart(userId);
        
        log.info("Order created successfully: {}", savedOrder.getOrderNumber());
        return convertToDTO(savedOrder);
    }
    
    public OrderDTO getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        return convertToDTO(order);
    }
    
    public List<OrderDTO> getUserOrders(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public void updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        order.setStatus(status);
        orderRepository.save(order);
        log.info("Order {} status updated to: {}", orderId, status);
    }
    
    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    private OrderDTO convertToDTO(Order order) {
        return new OrderDTO(
                order.getId(),
                order.getUserId(),
                order.getOrderNumber(),
                order.getItems().stream()
                        .map(item -> new OrderItemDTO(
                                item.getId(),
                                item.getProductId(),
                                item.getProductName(),
                                item.getQuantity(),
                                item.getUnitPrice(),
                                item.getTotalPrice()
                        ))
                        .collect(Collectors.toList()),
                order.getTotalAmount(),
                order.getStatus(),
                order.getShippingAddress(),
                order.getBillingAddress()
        );
    }
}
