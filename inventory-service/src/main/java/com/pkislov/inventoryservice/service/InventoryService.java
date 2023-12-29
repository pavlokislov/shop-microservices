package com.pkislov.inventoryservice.service;

import com.pkislov.inventoryservice.dto.InventoryRequest;
import com.pkislov.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import com.pkislov.inventoryservice.module.Inventory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public boolean isInStock(final String skuCode) {
        return inventoryRepository.existsBySkuCode(skuCode);
    }

    public void putInInventory(final InventoryRequest inventoryRequest) {
        Inventory inventory = new Inventory();
        inventory.setSkuCode(inventoryRequest.getSkuCode());
        inventoryRepository.save(inventory);
    }
}
