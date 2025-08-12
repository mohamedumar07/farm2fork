package com.grocery.inventoryservice.service;

import com.grocery.inventoryservice.dto.InventoryDto;
import com.grocery.inventoryservice.dto.InventorySaveDto;

import java.util.List;

public interface InventoryService {
    InventoryDto addInventory(InventorySaveDto inventorySaveDto);
    boolean isProductAvailableInInventory(long productId);

    boolean isStockAvailable(long productId,long requiredQuantity);

    InventoryDto getInventoryByProductId(Long productId);
    List<InventoryDto> getAllInventory();

    InventoryDto updateInventoryAfterOrder(Long productId, Long requiredQuantity);
    InventoryDto updateStock(Long productId, Long quantity);
    void deleteInventory(Long productId);

    List<InventoryDto> getInventoryByWarehouseLocation(String warehouseLocation);
}
