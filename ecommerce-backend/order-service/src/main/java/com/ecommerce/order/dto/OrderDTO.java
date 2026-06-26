package com.ecommerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private Long userId;
    private String orderNumber;
    private List<OrderItemDTO> items;
    private BigDecimal totalAmount;
    private String status;
    private String shippingAddress;
    private String billingAddress;
}
