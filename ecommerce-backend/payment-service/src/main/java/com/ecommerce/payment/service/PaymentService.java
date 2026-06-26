package com.ecommerce.payment.service;

import com.ecommerce.payment.dto.PaymentDTO;
import com.ecommerce.payment.entity.Payment;
import com.ecommerce.payment.repository.PaymentRepository;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class PaymentService {
    
    private final PaymentRepository paymentRepository;
    
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
    
    public PaymentDTO getPaymentByOrderId(Long orderId) {
        log.info("Fetching payment for order: {}", orderId);
        
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for order: " + orderId));
        
        return convertToDTO(payment);
    }
    
    public PaymentDTO getPayment(Long paymentId) {
        log.info("Fetching payment: {}", paymentId);
        
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));
        
        return convertToDTO(payment);
    }
    
    public List<PaymentDTO> getUserPayments(Long userId) {
        log.info("Fetching payments for user: {}", userId);
        
        return paymentRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private PaymentDTO convertToDTO(Payment payment) {
        return new PaymentDTO(
                payment.getId(),
                payment.getOrderId(),
                payment.getUserId(),
                payment.getTransactionId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getPaymentMethod(),
                payment.getRemarks()
        );
    }
}
