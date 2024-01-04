package com.pkislov.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pkislov.productservice.dto.ProductRequest;
import com.pkislov.productservice.dto.ProductResponse;
import com.pkislov.productservice.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest()
@ContextConfiguration(classes = ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean(name = "productService")
    private ProductService productService;

    @Test
    void testCreateProduct_ReturnsHttpStatusCreated_WhenProductRequestIsValid() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Test Product");
        productRequest.setDescription("Test Description");
        productRequest.setPrice(BigDecimal.valueOf(10.0));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(productRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void testGetAllProducts_ReturnsListOfProductResponses_WhenProductsExist() throws Exception {
        List<ProductResponse> products = Arrays.asList(
                new ProductResponse("1", "Product 1", "Description 1", BigDecimal.valueOf(10.0)),
                new ProductResponse("2", "Product 2", "Description 2", BigDecimal.valueOf(20.0))
        );

        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/product")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Product 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("Description 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].price").value(10.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Product 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value("Description 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].price").value(20.0));
    }

    private static String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
