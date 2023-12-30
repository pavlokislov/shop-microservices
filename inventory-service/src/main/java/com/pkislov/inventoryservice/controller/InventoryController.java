package com.pkislov.inventoryservice.controller;

import com.pkislov.inventoryservice.dto.InventoryDto;
import com.pkislov.inventoryservice.service.InventoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping()
    public boolean isInStock(@RequestParam("skuCode") List<String> skuCode) {
        return inventoryService.isInStock(skuCode);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void putInInventory(@RequestBody InventoryDto inventoryRequest) {
        inventoryService.putInInventory(inventoryRequest);
    }
}
