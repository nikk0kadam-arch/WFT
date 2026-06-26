package com.ecommerce.inventory.service;

import com.ecommerce.inventory.dto.InventoryDTO;
import com.ecommerce.inventory.entity.Inventory;
import com.ecommerce.inventory.entity.InventoryTransaction;
import com.ecommerce.inventory.repository.InventoryRepository;
import com.ecommerce.inventory.repository.InventoryTransactionRepository;
import com.ecommerce.shared.exception.BusinessException;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class InventoryService {
    
    private final InventoryRepository inventoryRepository;
    private final InventoryTransactionRepository inventoryTransactionRepository;
    
    public InventoryService(InventoryRepository inventoryRepository,
                           InventoryTransactionRepository inventoryTransactionRepository) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryTransactionRepository = inventoryTransactionRepository;
    }
    
    public InventoryDTO getInventory(Long productId) {
        log.info("Fetching inventory for product: {}", productId);
        
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product: " + productId));
        
        return convertToDTO(inventory);
    }
    
    public void reserveInventory(Long productId, Long orderId, Integer quantity) {
        log.info("Reserving {} units of product {} for order {}", quantity, productId, orderId);
        
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product: " + productId));
        
        if (inventory.getAvailableQuantity() < quantity) {
            throw new BusinessException("Insufficient inventory", "INSUFFICIENT_INVENTORY", 400);
        }
        
        // Reduce available and increase reserved
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() - quantity);
        inventory.setReservedQuantity(inventory.getReservedQuantity() + quantity);
        inventoryRepository.save(inventory);
        
        // Log transaction
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setProductId(productId);
        transaction.setOrderId(orderId);
        transaction.setTransactionType("RESERVED");
        transaction.setQuantity(quantity);
        transaction.setRemarks("Reserved for order " + orderId);
        inventoryTransactionRepository.save(transaction);
    }
    
    public void confirmReservation(Long orderId) {
        log.info("Confirming inventory reservation for order: {}", orderId);
        
        // Find all reserved items for this order
        java.util.List<InventoryTransaction> transactions = inventoryTransactionRepository.findByOrderId(orderId);
        
        for (InventoryTransaction transaction : transactions) {
            if ("RESERVED".equals(transaction.getTransactionType())) {
                Inventory inventory = inventoryRepository.findByProductId(transaction.getProductId())
                        .orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));
                
                // Reduce reserved quantity (don't increase available - it was already reduced)
                inventory.setReservedQuantity(Math.max(0, inventory.getReservedQuantity() - transaction.getQuantity()));
                inventoryRepository.save(inventory);
                
                // Log confirmation transaction
                InventoryTransaction confirmTransaction = new InventoryTransaction();
                confirmTransaction.setProductId(transaction.getProductId());
                confirmTransaction.setOrderId(orderId);
                confirmTransaction.setTransactionType("CONFIRMED");
                confirmTransaction.setQuantity(transaction.getQuantity());
                confirmTransaction.setRemarks("Confirmed after payment for order " + orderId);
                inventoryTransactionRepository.save(confirmTransaction);
            }
        }
    }
    
    public void releaseReservation(Long orderId) {
        log.info("Releasing inventory reservation for order: {}", orderId);
        
        // Find all reserved items for this order
        java.util.List<InventoryTransaction> transactions = inventoryTransactionRepository.findByOrderId(orderId);
        
        for (InventoryTransaction transaction : transactions) {
            if ("RESERVED".equals(transaction.getTransactionType())) {
                Inventory inventory = inventoryRepository.findByProductId(transaction.getProductId())
                        .orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));
                
                // Restore available and reduce reserved
                inventory.setAvailableQuantity(inventory.getAvailableQuantity() + transaction.getQuantity());
                inventory.setReservedQuantity(Math.max(0, inventory.getReservedQuantity() - transaction.getQuantity()));
                inventoryRepository.save(inventory);
                
                // Log release transaction
                InventoryTransaction releaseTransaction = new InventoryTransaction();
                releaseTransaction.setProductId(transaction.getProductId());
                releaseTransaction.setOrderId(orderId);
                releaseTransaction.setTransactionType("RELEASED");
                releaseTransaction.setQuantity(transaction.getQuantity());
                releaseTransaction.setRemarks("Released after payment failure for order " + orderId);
                inventoryTransactionRepository.save(releaseTransaction);
            }
        }
    }
    
    private InventoryDTO convertToDTO(Inventory inventory) {
        return new InventoryDTO(
                inventory.getId(),
                inventory.getProductId(),
                inventory.getReservedQuantity(),
                inventory.getAvailableQuantity()
        );
    }
}
