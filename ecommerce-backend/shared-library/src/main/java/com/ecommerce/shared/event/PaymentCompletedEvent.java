package com.ecommerce.shared.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCompletedEvent {
    private Long paymentId;
    private Long orderId;
    private Long userId;
    private BigDecimal amount;
    private String status;
    private String paymentMethod;
    private LocalDateTime completedAt;
}
