package com.pkislov.inventoryservice.service;

import com.pkislov.inventoryservice.dto.InventoryDto;
import com.pkislov.inventoryservice.exeception.DuplicatedSkuCodeException;
import com.pkislov.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import com.pkislov.inventoryservice.module.Inventory;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private static final String DUPLICATED_SKU_CODE_ERROR = "SkuCode {%s} is already exist";
    private final InventoryRepository inventoryRepository;

    public boolean isInStock(final List<String> skuCodes) {
        return inventoryRepository.existsBySkuCodeIn(skuCodes);
    }

    public void saveInInventory(final InventoryDto inventoryRequest) {
        if (isSkuCodeDuplicated(inventoryRequest)) {
            throw new DuplicatedSkuCodeException(format(DUPLICATED_SKU_CODE_ERROR, inventoryRequest.getSkuCode()));
        }

        Inventory inventory = new Inventory();
        inventory.setSkuCode(inventoryRequest.getSkuCode());
        inventoryRepository.save(inventory);
    }

    private boolean isSkuCodeDuplicated(InventoryDto inventoryRequest) {
        return inventoryRepository.findAll().stream().map(Inventory::getSkuCode)
                .anyMatch(skuCode -> skuCode.equals(inventoryRequest.getSkuCode()));
    }

    public boolean deleteInInventory(final String skuCode) {
        return inventoryRepository.deleteBySkuCode(skuCode);
    }
}
