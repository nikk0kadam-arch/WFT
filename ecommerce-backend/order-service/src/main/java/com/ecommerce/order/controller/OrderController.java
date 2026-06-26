package com.ecommerce.order.controller;

import com.ecommerce.order.dto.CreateOrderRequest;
import com.ecommerce.order.dto.OrderDTO;
import com.ecommerce.order.service.OrderService;
import com.ecommerce.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order Management", description = "Order endpoints")
@Slf4j
public class OrderController {
    
    private final OrderService orderService;
    
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    @PostMapping
    @Operation(summary = "Create new order from cart")
    public ResponseEntity<ApiResponse<OrderDTO>> createOrder(@RequestBody CreateOrderRequest request) {
        log.info("Creating order for user: {}", request.getUserId());
        
        OrderDTO orderDTO = orderService.createOrder(
                request.getUserId(),
                request.getShippingAddress(),
                request.getBillingAddress()
        );
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(orderDTO, "Order created successfully"));
    }
    
    @GetMapping("/{orderId}")
    @Operation(summary = "Get order by ID")
    public ResponseEntity<ApiResponse<OrderDTO>> getOrder(@PathVariable Long orderId) {
        log.info("Fetching order: {}", orderId);
        
        OrderDTO orderDTO = orderService.getOrder(orderId);
        return ResponseEntity.ok(ApiResponse.success(orderDTO, "Order retrieved successfully"));
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get all orders for user")
    public ResponseEntity<ApiResponse<List<OrderDTO>>> getUserOrders(@PathVariable Long userId) {
        log.info("Fetching orders for user: {}", userId);
        
        List<OrderDTO> orders = orderService.getUserOrders(userId);
        return ResponseEntity.ok(ApiResponse.success(orders, "Orders retrieved successfully"));
    }
    
    @PutMapping("/{orderId}/status")
    @Operation(summary = "Update order status")
    public ResponseEntity<ApiResponse<String>> updateOrderStatus(@PathVariable Long orderId,
                                                                @RequestParam String status) {
        log.info("Updating order {} status to: {}", orderId, status);
        
        orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(ApiResponse.success(null, "Order status updated"));
    }
}
