package com.pkislov.inventoryservice.controller;

import com.pkislov.inventoryservice.dto.InventoryDto;
import com.pkislov.inventoryservice.service.InventoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@RequestParam("skuCode") List<String> skuCodes) {
        return inventoryService.isInStock(skuCodes);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void saveInInventory(@RequestBody @Validated InventoryDto inventoryRequest) {
        inventoryService.saveInInventory(inventoryRequest);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.OK)
    public void deleteInInventory(@RequestParam("skuCode") String skuCode) {
        if (!inventoryService.deleteInInventory(skuCode)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Provided SkuCode not found in Inventory");
        }
    }
}
