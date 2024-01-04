package com.pkislov.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Builder;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItemsDto {

    @NonNull
    private String skuCode;
    @NonNull
    private BigDecimal price;
    @NonNull
    private Integer quantity;
}
