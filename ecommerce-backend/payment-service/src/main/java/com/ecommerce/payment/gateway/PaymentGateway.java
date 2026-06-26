package com.ecommerce.payment.gateway;

import com.ecommerce.shared.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@Slf4j
public class PaymentGateway {
    
    /**
     * Simulated payment gateway that randomly succeeds or fails for demonstration
     * In production, this would integrate with real payment providers (Stripe, PayPal, etc.)
     */
    public PaymentResult processPayment(Long orderId, String cardNumber, BigDecimal amount) {
        log.info("Processing payment for order {} with amount {}", orderId, amount);
        
        // Validate card number (simple demo validation)
        if (cardNumber == null || cardNumber.length() < 13) {
            throw new BusinessException("Invalid card number", "INVALID_CARD", 400);
        }
        
        // Simulate payment processing
        try {
            // In real scenario, call actual payment gateway API here
            Thread.sleep(500);
            
            // Simulate 90% success rate (9 out of 10 succeed)
            if (Math.random() < 0.9) {
                String transactionId = generateTransactionId();
                log.info("Payment processed successfully with transaction ID: {}", transactionId);
                return new PaymentResult(true, transactionId, "Payment successful");
            } else {
                log.warn("Payment processing failed");
                return new PaymentResult(false, null, "Payment declined by gateway");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException("Payment processing interrupted", "PAYMENT_ERROR", 500);
        }
    }
    
    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 16).toUpperCase();
    }
    
    public static class PaymentResult {
        public final boolean success;
        public final String transactionId;
        public final String message;
        
        public PaymentResult(boolean success, String transactionId, String message) {
            this.success = success;
            this.transactionId = transactionId;
            this.message = message;
        }
    }
}
