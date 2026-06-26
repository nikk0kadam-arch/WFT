package com.ecommerce.inventory.controller;

import com.ecommerce.inventory.dto.InventoryDTO;
import com.ecommerce.inventory.service.InventoryService;
import com.ecommerce.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@Tag(name = "Inventory Management", description = "Inventory endpoints")
@Slf4j
public class InventoryController {
    
    private final InventoryService inventoryService;
    
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }
    
    @GetMapping("/product/{productId}")
    @Operation(summary = "Get inventory for product")
    public ResponseEntity<ApiResponse<InventoryDTO>> getInventory(@PathVariable Long productId) {
        log.info("Fetching inventory for product: {}", productId);
        
        InventoryDTO inventoryDTO = inventoryService.getInventory(productId);
        return ResponseEntity.ok(ApiResponse.success(inventoryDTO, "Inventory retrieved successfully"));
    }
    
    @PostMapping("/reserve")
    @Operation(summary = "Reserve inventory for order")
    public ResponseEntity<ApiResponse<String>> reserveInventory(@RequestParam Long productId,
                                                               @RequestParam Long orderId,
                                                               @RequestParam Integer quantity) {
        log.info("Reserving {} units of product {} for order {}", quantity, productId, orderId);
        
        inventoryService.reserveInventory(productId, orderId, quantity);
        return ResponseEntity.ok(ApiResponse.success(null, "Inventory reserved successfully"));
    }
}
