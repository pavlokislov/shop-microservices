package com.pkislov.inventoryservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequest {

    @NonNull
    private String skuCode;

    @NonNull
    private Integer quantity;
}
