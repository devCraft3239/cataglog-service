package com.dev_craft.catalogservice.Dto;

import lombok.Data;

@Data
public class ProductInventoryResponse {
    private Long id;
    private String productCode;
    private Integer availableQuantity = 0;
}