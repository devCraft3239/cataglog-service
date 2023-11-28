package com.dev_craft.catalogservice.client;

import com.dev_craft.catalogservice.Dto.ProductInventoryResponse;;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "inventory-service")
public interface InventoryServiceFeignClient {

    @GetMapping("/api/inventory")
    List<ProductInventoryResponse> getInventoryLevels();

    @GetMapping("/api/inventory/{productCode}")
    ResponseEntity<ProductInventoryResponse> findInventoryByProductCode(@PathVariable("productCode") String productCode);
}