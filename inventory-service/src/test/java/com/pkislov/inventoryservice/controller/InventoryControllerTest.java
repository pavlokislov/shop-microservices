package com.pkislov.inventoryservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pkislov.inventoryservice.dto.InventoryDto;
import com.pkislov.inventoryservice.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.mockito.Mockito.when;

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    private static final String INVENTORY_ENDPOINT = "/api/inventory";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryService inventoryService;

    @Test
    void testIsInStock() throws Exception {
        when(inventoryService.isInStock(Arrays.asList("skuCode1", "skuCode2"))).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get(INVENTORY_ENDPOINT)
                        .param("skuCode", "skuCode1", "skuCode2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }

    @Test
    void testSaveInInventory() throws Exception {
        InventoryDto inventoryDto = new InventoryDto();
        inventoryDto.setSkuCode("skuCode1");
        inventoryDto.setQuantity(10);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(inventoryDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void testDeleteInInventory() throws Exception {
        when(inventoryService.deleteInInventory("skuCode1")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/inventory")
                        .param("skuCode", "skuCode1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private String asJsonString(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
