package com.ecommerce.shared.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryUpdatedEvent {
    private Long productId;
    private Integer quantityChange;
    private Integer newStock;
    private String transactionType;
    private Long referenceId;
    private LocalDateTime updatedAt;
}
