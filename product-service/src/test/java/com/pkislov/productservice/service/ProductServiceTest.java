package com.pkislov.productservice.service;

import com.pkislov.productservice.dto.ProductRequest;
import com.pkislov.productservice.dto.ProductResponse;
import com.pkislov.productservice.model.Product;
import com.pkislov.productservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private ProductService sut;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sut = new ProductService(productRepository);
    }

    @Test
    void testCreateProduct_SavesProduct_WhenProductRequestIsValid() {
        // given
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Test Product");
        productRequest.setDescription("Test Description");
        productRequest.setPrice(BigDecimal.valueOf(10));

        when(productRepository.save(any(Product.class))).thenReturn(new Product());

        // when
        sut.createProduct(productRequest);

        // then
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testGetAllProducts_ReturnsListOfProductResponses_WhenProductsExist() {
        // given
        List<Product> products = Arrays.asList(
                new Product("1", "Product 1", "Description 1", BigDecimal.valueOf(10.0)),
                new Product("2", "Product 2", "Description 2", BigDecimal.valueOf(20.0))
        );

        when(productRepository.findAll()).thenReturn(products);

        // when
        List<ProductResponse> result = sut.getAllProducts();

        // then
        assertEquals(2, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("Product 1", result.get(0).getName());
        assertEquals("Description 1", result.get(0).getDescription());
        assertEquals(BigDecimal.valueOf(10.0), result.get(0).getPrice());
        assertEquals("2", result.get(1).getId());
        assertEquals("Product 2", result.get(1).getName());
        assertEquals("Description 2", result.get(1).getDescription());
        assertEquals(BigDecimal.valueOf(20.0), result.get(1).getPrice());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetAllProducts_ReturnsEmptyList_WhenNoProductsExist() {
        // given
        when(productRepository.findAll()).thenReturn(Arrays.asList());

        // when
        List<ProductResponse> result = sut.getAllProducts();

        // then
        assertTrue(result.isEmpty());
        verify(productRepository, times(1)).findAll();
    }
}
