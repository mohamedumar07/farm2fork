package com.grocery.inventoryservice.service;

import com.grocery.inventoryservice.client.ProductClient;
import com.grocery.inventoryservice.client.ProductResponseDto;
import com.grocery.inventoryservice.dto.InventoryDto;
import com.grocery.inventoryservice.dto.InventorySaveDto;
import com.grocery.inventoryservice.entity.Inventory;
import com.grocery.inventoryservice.exception.InventoryAlreadyExistsException;
import com.grocery.inventoryservice.exception.InventoryNotAvailable;
import com.grocery.inventoryservice.exception.ProductNotAvailable;
import com.grocery.inventoryservice.mapper.InventoryMapper;
import com.grocery.inventoryservice.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceImp implements InventoryService {

    @Value("${product.client.url}")
    private String productBaseUrl;

    private final InventoryRepository inventoryRepository;

    private final InventoryMapper inventoryMapper;

    private final ProductClient productClient;

    public InventoryServiceImp(InventoryRepository inventoryRepository, InventoryMapper inventoryMapper, ProductClient productClient) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryMapper = inventoryMapper;
        this.productClient = productClient;
    }

    @Override
    public InventoryDto addInventory(InventorySaveDto inventorySaveDto) {
        boolean inventoryExists = inventoryRepository.existsByProductIdAndWarehouseLocation(
                                    inventorySaveDto.getProductId(),inventorySaveDto.getWarehouseLocation());

        if(inventoryExists==true){
            throw new InventoryAlreadyExistsException("Inventory already exists in the location :" + inventorySaveDto.getWarehouseLocation());
        }

        Inventory inventory = inventoryMapper.inventorySaveDtoToInventory(inventorySaveDto);

        Inventory savedInventory = inventoryRepository.save(inventory);

        var getProductDetails = productClient.getByProductId(inventory.getProductId());

        return convertToInventoryDto(savedInventory,getProductDetails);
    }

    @Override
    public boolean isProductAvailableInInventory(long productId) {

        Inventory productAvailableInInventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(()-> new ProductNotAvailable("Product Not Available in the inventory"));

        if(productAvailableInInventory.getQuantity()>0){
            return true;
        }

        return false;
    }

    @Override
    public boolean isStockAvailable(long productId,long requiredQuantity) {

        boolean inventoryAvailable = inventoryRepository.existsByProductIdAndQuantityGreaterThanEqual(productId,requiredQuantity);

        return inventoryAvailable;
    }

    @Override
    public InventoryDto getInventoryByProductId(Long productId) {

        Optional<Inventory> inventoryExists = inventoryRepository.findByProductId(productId);

        if(inventoryExists.isEmpty()){
            throw new InventoryNotAvailable("Inventory not available for the product :" + productId);
        }

        var getProductDetails = productClient.getByProductId(productId);
        Inventory inventory = inventoryExists.get();

        return convertToInventoryDto(inventory,getProductDetails);
    }

    @Override
    public List<InventoryDto> getAllInventory() {
        List<Inventory> inventoryList = inventoryRepository.findAll();
        List<InventoryDto> inventoryDtoList = new ArrayList<>();

        for(Inventory inventory : inventoryList){
            var getProductDetails = productClient.getByProductId(inventory.getProductId());

            InventoryDto inventoryDto = convertToInventoryDto(inventory,getProductDetails);

            inventoryDtoList.add(inventoryDto);
        }

        return inventoryDtoList;
    }

    @Override
    public InventoryDto updateInventoryAfterOrder(Long productId, Long requiredQuantity) {

        Inventory inventoryExists = inventoryRepository.findByProductId(productId)
                .orElseThrow(()-> new InventoryNotAvailable("Inventory not available for the product :" + productId));

        if(inventoryExists.getQuantity()<requiredQuantity){
            throw new ProductNotAvailable("Product not available. Required Quantity is more than the Inventory Quantity for the :" + productId);
        }

        long newQuantity = inventoryExists.getQuantity()-requiredQuantity;
        inventoryExists.setQuantity(newQuantity);

        Inventory updatedInventory = inventoryRepository.save(inventoryExists);
        var getProductDetails = productClient.getByProductId(productId);

        return convertToInventoryDto(updatedInventory,getProductDetails);
    }

    @Override
    public InventoryDto updateStock(Long productId, Long quantity) {
        Inventory inventoryExists = inventoryRepository.findByProductId(productId)
                .orElseThrow(()-> new InventoryNotAvailable("Inventory not available for the product :" + productId) );
        if(quantity < 0){
            throw new RuntimeException("Quantity should be greater than zero");
        }
        inventoryExists.setQuantity(quantity);
        Inventory updatedInventory = inventoryRepository.save(inventoryExists);

        var getProductDetails = productClient.getByProductId(productId);

        return convertToInventoryDto(updatedInventory,getProductDetails);
    }

    @Override
    public void deleteInventory(Long productId) {
        Optional<Inventory> inventoryExists = inventoryRepository.findByProductId(productId);
        if(inventoryExists.isEmpty()){
            throw new InventoryNotAvailable("Inventory not available for the product :" + productId);
        }
        inventoryRepository.deleteById(productId);
    }

    @Override
    public List<InventoryDto> getInventoryByWarehouseLocation(String warehouseLocation) {
        List<Inventory> getInventoryByWarehouse = inventoryRepository.findAllByWarehouseLocation(warehouseLocation);

        List<InventoryDto> inventoryDtoList = new ArrayList<>();

        for(Inventory inventory : getInventoryByWarehouse){
            var getProductDetails = productClient.getByProductId(inventory.getProductId());
            InventoryDto inventoryDto = convertToInventoryDto(inventory,getProductDetails);
            inventoryDtoList.add(inventoryDto);
        }

        return inventoryDtoList;
    }

    private InventoryDto convertToInventoryDto(Inventory inventory, ProductResponseDto productResponseDto){
        InventoryDto inventoryResponseDto = inventoryMapper.inventoryToInventoryDto(inventory);

        inventoryResponseDto.setProductName(productResponseDto.getProductName());
        inventoryResponseDto.setProductCategory(productResponseDto.getProductCategory());
        inventoryResponseDto.setProductBrand(productResponseDto.getBrandName());
        inventoryResponseDto.setPrice(productResponseDto.getPrice());
        String posterUrl = productBaseUrl + "/api/v1/file/"+ productResponseDto.getProductPoster();
        inventoryResponseDto.setProductPoster(posterUrl);

        return inventoryResponseDto;
    }


}
