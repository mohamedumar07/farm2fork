package com.grocery.inventoryservice.repository;

import com.grocery.inventoryservice.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    boolean existsByProductIdAndWarehouseLocation(long productId, String warehouseLocation);

    List<Inventory> findAllByProductId(long productId);

    boolean existsByProductIdAndQuantityGreaterThanEqual(long productId, long requiredQuantity);

    Optional<Inventory> findByProductId(Long productId);

    List<Inventory> findAllByWarehouseLocation(String warehouseLocation);

}
