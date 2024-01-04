package com.pkislov.inventoryservice.service;

import com.pkislov.inventoryservice.dto.InventoryDto;
import com.pkislov.inventoryservice.exeception.DuplicatedSkuCodeException;
import com.pkislov.inventoryservice.module.Inventory;
import com.pkislov.inventoryservice.repository.InventoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventoryServiceTest {

    private InventoryService sut;

    @Mock
    private InventoryRepository inventoryRepository;


    private AutoCloseable autoCloseableMocks;

    @BeforeEach()
    public void beforeTest() {
        autoCloseableMocks = MockitoAnnotations.openMocks(this);
        sut = new InventoryService(inventoryRepository);
    }

    @AfterEach()
    public void afterTest() throws Exception {
        if (autoCloseableMocks != null)
            autoCloseableMocks.close();
    }


    @Test
    void testIsInStock_ReturnsTrue_WhenAllSkuCodesAreInStock() {
        // given
        List<String> skuCodes = Arrays.asList("skuCode1", "skuCode2");
        when(inventoryRepository.existsBySkuCodeIn(skuCodes)).thenReturn(true);

        // when
        boolean result = sut.isInStock(skuCodes);

        // then
        assertTrue(result);
        verify(inventoryRepository, times(1)).existsBySkuCodeIn(skuCodes);
    }

    @Test
    void testIsInStock_ReturnsFalse_WhenAnySkuCodeIsNotInStock() {
        // given
        List<String> skuCodes = Arrays.asList("skuCode1", "skuCode2");
        when(inventoryRepository.existsBySkuCodeIn(skuCodes)).thenReturn(false);

        // when
        boolean result = sut.isInStock(skuCodes);

        // then
        assertFalse(result);
        verify(inventoryRepository, times(1)).existsBySkuCodeIn(skuCodes);
    }

    @Test
    void testSaveInInventory_ThrowsDuplicatedSkuCodeException_WhenSkuCodeIsNotUnique() {
        // given
        InventoryDto inventoryDto = new InventoryDto();
        inventoryDto.setSkuCode("skuCode1");

        // when
        when(inventoryRepository.findAll()).thenReturn(Arrays.asList(new Inventory(1L, "skuCode1", 20)));

        // then
        assertThrows(DuplicatedSkuCodeException.class, () -> sut.saveInInventory(inventoryDto));
        verify(inventoryRepository, times(1)).findAll();
    }

    @Test
    void testSaveInInventory_SavesInventory_WhenSkuCodeIsUnique() {
        // given
        InventoryDto inventoryDto = new InventoryDto();
        inventoryDto.setSkuCode("skuCode1");

        when(inventoryRepository.findAll()).thenReturn(Arrays.asList(new Inventory(2L, "skuCode2", 30)));

        // when
        sut.saveInInventory(inventoryDto);

        // then
        verify(inventoryRepository, times(1)).save(any(Inventory.class));
    }

    @Test
    void testDeleteInInventory_ReturnsTrue_WhenSkuCodeIsDeleted() {
        // given
        String skuCode = "skuCode1";
        when(inventoryRepository.deleteBySkuCode(skuCode)).thenReturn(true);

        // when
        boolean result = sut.deleteInInventory(skuCode);

        // then
        assertTrue(result);
        verify(inventoryRepository, times(1)).deleteBySkuCode(skuCode);
    }

    @Test
    void testDeleteInInventory_ReturnsFalse_WhenSkuCodeIsNotFound() {
        // given
        String skuCode = "skuCode1";
        when(inventoryRepository.deleteBySkuCode(skuCode)).thenReturn(false);

        // when
        boolean result = sut.deleteInInventory(skuCode);

        // then
        assertFalse(result);
        verify(inventoryRepository, times(1)).deleteBySkuCode(skuCode);
    }
}
