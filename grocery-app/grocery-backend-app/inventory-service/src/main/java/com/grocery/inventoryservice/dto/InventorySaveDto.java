package com.grocery.inventoryservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InventorySaveDto {

    @NotNull(message = "Product Id Required.")
    private long productId;

    @NotNull(message = "Quantity Required.")
    private long quantity;

    @NotNull(message = "WareHouse Location Required.")
    private String warehouseLocation;
}
