package com.grocery.inventoryservice.controller;

import com.grocery.inventoryservice.dto.InventoryDto;
import com.grocery.inventoryservice.dto.InventorySaveDto;
import com.grocery.inventoryservice.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@CrossOrigin("*")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }


    @GetMapping("/productStockAvailable/{product_id}")
    public ResponseEntity<?> productStockAvailable(@PathVariable("product_id") Long productId) {
        boolean productStockAvailable = inventoryService.isProductAvailableInInventory(productId);
        if (productStockAvailable) {
            return new ResponseEntity<>(true,HttpStatus.OK);
        }
        return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);
    }

    @GetMapping("/isStockAvailable/{product_id}/{required_quantity}")
    public ResponseEntity<?> stockAvailableForProduct(
            @PathVariable("product_id") long productId,
            @PathVariable("required_quantity") long requiredQuantity
    ){
        boolean isStockAvailable = inventoryService.isStockAvailable(productId, requiredQuantity);
        if (isStockAvailable) {
            return new ResponseEntity<>(true,HttpStatus.OK);
        }
        return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);
    }

    @PutMapping("/updateStockAfterOrder/{product_id}/{required_quantity}")
    public ResponseEntity<?> updateStockAfterOrder(
            @PathVariable("product_id") long productId,
            @PathVariable("required_quantity") long requiredQuantity
    ){
        InventoryDto updateInventoryAfterOrdering = inventoryService.updateInventoryAfterOrder(productId, requiredQuantity);
        return new ResponseEntity<>(updateInventoryAfterOrdering, HttpStatus.OK);
    }

    //Admin Actions
    @GetMapping("/all")
    public ResponseEntity<?> getAllInventory() {
        List<InventoryDto> inventoryDtoList = inventoryService.getAllInventory();
        if (inventoryDtoList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(inventoryDtoList, HttpStatus.OK);
    }

    @PutMapping("/updateStock/{product_id}/{quantity}")
    public ResponseEntity<?> updateStockForProduct(
            @PathVariable("product_id") long productId,
            @PathVariable("quantity") long quantity
    ){
        InventoryDto updateInventory = inventoryService.updateStock(productId, quantity);
        return new ResponseEntity<>(updateInventory, HttpStatus.OK);
    }

    @GetMapping("/allByWarehouse/{warehouse_location}")
    public ResponseEntity<?> getInventoryByWarehouse(
            @PathVariable("warehouse_location") String warehouseLocation) {
        List<InventoryDto> getByWarehouseLocation = inventoryService.getInventoryByWarehouseLocation(warehouseLocation);
        if(getByWarehouseLocation.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(getByWarehouseLocation, HttpStatus.OK);
    }

    @GetMapping("/getById/{product_id}")
    public ResponseEntity<?> getInventoryById(@PathVariable("product_id") long productId) {
        InventoryDto getByProductId = inventoryService.getInventoryByProductId(productId);
        return new ResponseEntity<>(getByProductId, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addInventory(@RequestBody InventorySaveDto inventorySaveDto) {
        InventoryDto addInventory = inventoryService.addInventory(inventorySaveDto);
        return new ResponseEntity<>(addInventory, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{product_id}")
    public ResponseEntity<?> deleteInventory(@PathVariable("product_id") long productId) {
        inventoryService.deleteInventory(productId);
        return new ResponseEntity<>("Inventory removed Successfully", HttpStatus.OK);
    }




}
