package com.ecommerce.shared.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {
    private Long orderId;
    private Long userId;
    private BigDecimal orderAmount;
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime createdAt;
}
