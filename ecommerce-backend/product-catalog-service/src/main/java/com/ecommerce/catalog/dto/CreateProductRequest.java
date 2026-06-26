package com.ecommerce.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal cost;
    private String sku;
    private Long categoryId;
    private Integer stockQuantity;
}
