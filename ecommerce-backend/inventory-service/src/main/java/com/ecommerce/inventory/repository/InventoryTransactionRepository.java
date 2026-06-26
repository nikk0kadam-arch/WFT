package com.ecommerce.inventory.repository;

import com.ecommerce.inventory.entity.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {
    List<InventoryTransaction> findByOrderId(Long orderId);
    List<InventoryTransaction> findByProductId(Long productId);
}
