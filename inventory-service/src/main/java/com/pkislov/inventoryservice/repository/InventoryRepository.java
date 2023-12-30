package com.pkislov.inventoryservice.repository;

import com.pkislov.inventoryservice.dto.InventoryDto;
import com.pkislov.inventoryservice.module.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    @Transactional(readOnly = true)
    boolean existsBySkuCodeIn(List<String> skuCode);

    @Transactional(readOnly = true)
    @Query("SELECT new com.pkislov.inventoryservice.dto.InventoryDto(i.skuCode, i.quantity) FROM Inventory i")
    List<InventoryDto> findBySkuCodeIn(List<String> skuCode);

}
