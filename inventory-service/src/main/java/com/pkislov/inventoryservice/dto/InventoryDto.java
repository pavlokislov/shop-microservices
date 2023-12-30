package com.pkislov.inventoryservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDto {

    @NonNull
    private String skuCode;

    @NonNull
    private Integer quantity;
}
