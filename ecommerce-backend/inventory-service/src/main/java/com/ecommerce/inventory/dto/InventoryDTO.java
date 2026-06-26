package com.ecommerce.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDTO {
    private Long id;
    private Long productId;
    private Integer reservedQuantity;
    private Integer availableQuantity;
    
    public Integer getTotalQuantity() {
        return reservedQuantity + availableQuantity;
    }
}
