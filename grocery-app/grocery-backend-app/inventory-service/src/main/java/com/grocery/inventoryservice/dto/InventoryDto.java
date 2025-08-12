package com.grocery.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InventoryDto {
    private long inventoryId;
    private long productId;
    private String productName;
    private String productCategory;
    private String productBrand;
    private double price;
    private String productPoster;
    private long quantity;
    private String warehouseLocation;
}
