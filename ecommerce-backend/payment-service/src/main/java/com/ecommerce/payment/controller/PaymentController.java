package com.ecommerce.payment.controller;

import com.ecommerce.payment.dto.PaymentDTO;
import com.ecommerce.payment.service.PaymentService;
import com.ecommerce.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payment Management", description = "Payment endpoints")
@Slf4j
public class PaymentController {
    
    private final PaymentService paymentService;
    
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    
    @GetMapping("/{paymentId}")
    @Operation(summary = "Get payment by ID")
    public ResponseEntity<ApiResponse<PaymentDTO>> getPayment(@PathVariable Long paymentId) {
        log.info("Fetching payment: {}", paymentId);
        
        PaymentDTO paymentDTO = paymentService.getPayment(paymentId);
        return ResponseEntity.ok(ApiResponse.success(paymentDTO, "Payment retrieved successfully"));
    }
    
    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get payment by order ID")
    public ResponseEntity<ApiResponse<PaymentDTO>> getPaymentByOrderId(@PathVariable Long orderId) {
        log.info("Fetching payment for order: {}", orderId);
        
        PaymentDTO paymentDTO = paymentService.getPaymentByOrderId(orderId);
        return ResponseEntity.ok(ApiResponse.success(paymentDTO, "Payment retrieved successfully"));
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get all payments for user")
    public ResponseEntity<ApiResponse<List<PaymentDTO>>> getUserPayments(@PathVariable Long userId) {
        log.info("Fetching payments for user: {}", userId);
        
        List<PaymentDTO> payments = paymentService.getUserPayments(userId);
        return ResponseEntity.ok(ApiResponse.success(payments, "Payments retrieved successfully"));
    }
}
