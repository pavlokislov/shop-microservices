package com.pkislov.inventoryservice.repository;

import com.pkislov.inventoryservice.module.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    @Transactional(readOnly = true)
    boolean existsBySkuCode(String skuCode);

}
