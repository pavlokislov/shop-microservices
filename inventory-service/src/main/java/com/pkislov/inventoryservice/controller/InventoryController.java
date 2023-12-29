package com.pkislov.inventoryservice.controller;

import com.pkislov.inventoryservice.dto.InventoryRequest;
import com.pkislov.inventoryservice.service.InventoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/{sku-code}")
    public boolean isInStock(@PathVariable("sku-code") String skuCode) {
        return inventoryService.isInStock(skuCode);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void putInInventory(@RequestBody InventoryRequest inventoryRequest) {
        inventoryService.putInInventory(inventoryRequest);
    }
}
