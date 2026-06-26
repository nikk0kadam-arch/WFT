package com.ecommerce.notification.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(nullable = false)
    private String type; // ORDER_CREATED, PAYMENT_SUCCESS, PAYMENT_FAILED, INVENTORY_LOW, SHIPMENT_READY
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String message;
    
    @Column(nullable = false)
    private Boolean read = false;
    
    @Column(nullable = false)
    private String channel = "IN_APP"; // IN_APP, EMAIL, SMS
    
    @Column(nullable = false)
    private String status = "SENT";
    
    @CreationTimestamp
    private LocalDateTime createdAt;
}
