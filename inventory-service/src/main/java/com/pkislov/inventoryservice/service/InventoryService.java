package com.pkislov.inventoryservice.service;

import com.pkislov.inventoryservice.dto.InventoryDto;
import com.pkislov.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import com.pkislov.inventoryservice.module.Inventory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public boolean isInStock(final List<String> skuCodes) {
        return inventoryRepository.existsBySkuCodeIn(skuCodes);
    }

    public void putInInventory(final InventoryDto inventoryRequest) {
        Inventory inventory = new Inventory();
        inventory.setSkuCode(inventoryRequest.getSkuCode());
        inventoryRepository.save(inventory);
    }
}
