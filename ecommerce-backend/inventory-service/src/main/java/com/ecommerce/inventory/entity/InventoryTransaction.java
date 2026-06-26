package com.ecommerce.inventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryTransaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long productId;
    
    @Column(nullable = false)
    private Long orderId;
    
    @Column(nullable = false)
    private String transactionType; // RESERVED, RELEASED, CONFIRMED
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(length = 500)
    private String remarks;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
}
