package com.pkislov.productservice.smoke;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pkislov.productservice.dto.ProductRequest;
import com.pkislov.productservice.model.Product;
import com.pkislov.productservice.repository.ProductRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SmokeTests {

    private static final String API_PRODUCT_URL = "/api/product";
    private static final String DOCKER_IMAGE_NAME = "mongo:4.4.2";
    private static final String SPRING_DATA_MONGODB_URI = "spring.data.mongodb.uri";
    private static final String PRODUCT_NAME = "iPhone 13";
    private static final String PRODUCT_DESCRIPTION = "Description iPhone 13";
    private static final int PRODUCT_PRICE = 1200;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(DOCKER_IMAGE_NAME)
            .withReuse(false);

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add(SPRING_DATA_MONGODB_URI, mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    @Order(1)
    @SneakyThrows
    void shouldCreateProduct() {
        ProductRequest productRequest = getProductRequest();
        String request = objectMapper.writeValueAsString(productRequest);

        mockMvc.perform(MockMvcRequestBuilders.post(API_PRODUCT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated());

        List<Product> actualProducts = productRepository.findAll();
        assertEquals(1, actualProducts.size());
        Product actualProduct = actualProducts.get(0);
        Assertions.assertAll(
                "Grouped Assertions of Product",
                () -> assertEquals(productRequest.getName(), actualProduct.getName()),
                () -> assertEquals(productRequest.getDescription(), actualProduct.getDescription()),
                () -> assertEquals(productRequest.getPrice(), actualProduct.getPrice()));
    }

    @Test
    @Order(2)
    @SneakyThrows
    void shouldGetProduct() {
        mockMvc.perform(MockMvcRequestBuilders.get(API_PRODUCT_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].size()").value("4"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(PRODUCT_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value(PRODUCT_DESCRIPTION))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].price").value(PRODUCT_PRICE));
    }

    private ProductRequest getProductRequest() {
        return ProductRequest.builder()
                .name(PRODUCT_NAME)
                .description(PRODUCT_DESCRIPTION)
                .price(BigDecimal.valueOf(PRODUCT_PRICE))
                .build();
    }
}
