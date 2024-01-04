package com.pkislov.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pkislov.orderservice.dto.OrderLineItemsDto;
import com.pkislov.orderservice.dto.OrderRequest;
import com.pkislov.orderservice.service.OrderService;
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
import java.util.List;

@WebMvcTest()
@ContextConfiguration(classes = OrderController.class)
class OrderControllerTest {

    private static final String ORDER_ENDPOINT = "/api/order";
    @Autowired
    private MockMvc mockMvc;

    @MockBean(name = "orderService")
    private OrderService orderService;

    @Test
    void testPlaceOrder_ReturnsHttpStatusCreated_WhenOrderRequestIsValid() throws Exception {
        OrderLineItemsDto dto =  OrderLineItemsDto.builder()
                .price(BigDecimal.ONE)
                .skuCode("sku-code")
                .quantity(5)
                .build();

        OrderRequest orderRequest = new OrderRequest(List.of(dto));

        mockMvc.perform(MockMvcRequestBuilders.post(ORDER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(orderRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void testPlaceOrder_ReturnsHttpStatusCreated_WhenOrderRequestIsInValid() throws Exception {
        OrderRequest orderRequest = new OrderRequest();

        mockMvc.perform(MockMvcRequestBuilders.post(ORDER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(orderRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    private static String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
