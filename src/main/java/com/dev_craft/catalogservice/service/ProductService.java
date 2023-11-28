package com.dev_craft.catalogservice.service;

import com.dev_craft.catalogservice.Dto.ProductInventoryResponse;
import com.dev_craft.catalogservice.client.InventoryServiceFeignClient;
import com.dev_craft.catalogservice.entity.Product;
import com.dev_craft.catalogservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final InventoryServiceFeignClient inventoryServiceClient;

    @Autowired
    public ProductService(ProductRepository productRepository, InventoryServiceFeignClient inventoryServiceClient) {
        this.productRepository = productRepository;
        this.inventoryServiceClient = inventoryServiceClient;
    }

    public List<Product> findAllProducts() {
        List<Product> products = productRepository.findAll();
        final Map<String, Integer> inventoryLevels = getInventoryLevelsWithFeignClient();
        final List<Product> availableProducts = products.stream()
                .filter(p -> inventoryLevels.get(p.getCode()) != null && inventoryLevels.get(p.getCode()) > 0)
                .collect(Collectors.toList());
        return availableProducts;
    }

    private Map<String, Integer> getInventoryLevelsWithFeignClient() {
        log.info("Fetching inventory levels using FeignClient");
        Map<String, Integer> inventoryLevels = new HashMap<>();
        List<ProductInventoryResponse> inventory = inventoryServiceClient.getInventoryLevels();
        for (ProductInventoryResponse item: inventory){
            inventoryLevels.put(item.getProductCode(), item.getAvailableQuantity());
        }
        log.debug("InventoryLevels: {}", inventoryLevels);
        return inventoryLevels;
    }


    public Optional<Product> findProductByCode(String code) {
        Optional<Product> productOptional = productRepository.findByCode(code);
        log.info(String.valueOf(productOptional.get()));
        if (productOptional.isPresent()) {
            Optional<ProductInventoryResponse> itemResponseEntity =
                    Optional.of(this.inventoryServiceClient.findInventoryByProductCode(code).getBody());
            if (itemResponseEntity.isPresent()) {
                log.info("found");
                Integer quantity = itemResponseEntity.get().getAvailableQuantity();
                productOptional.get().setInStock(quantity > 0);
            }else
                log.info("not found");
        }
        return productOptional;
    }
}