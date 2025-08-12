package com.grocery.inventoryservice.mapper;

import com.grocery.inventoryservice.dto.InventoryDto;
import com.grocery.inventoryservice.dto.InventorySaveDto;
import com.grocery.inventoryservice.entity.Inventory;
import org.springframework.stereotype.Component;

@Component
public class InventoryMapper {

    public static Inventory inventorySaveDtoToInventory(InventorySaveDto inventorySaveDto) {
        Inventory inventory = new Inventory();
        inventory.setProductId(inventorySaveDto.getProductId());
        inventory.setQuantity(inventorySaveDto.getQuantity());
        inventory.setWarehouseLocation(inventorySaveDto.getWarehouseLocation());
        return inventory;
    }

    public static InventoryDto inventoryToInventoryDto(Inventory inventory) {
        InventoryDto inventoryDto = new InventoryDto();
        inventoryDto.setInventoryId(inventory.getId());
        inventoryDto.setProductId(inventory.getProductId());
        inventoryDto.setQuantity(inventory.getQuantity());
        inventoryDto.setWarehouseLocation(inventory.getWarehouseLocation());
        return inventoryDto;
    }
}
