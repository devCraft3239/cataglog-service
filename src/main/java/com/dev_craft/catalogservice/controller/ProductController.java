package com.dev_craft.catalogservice.controller;

import com.dev_craft.catalogservice.entity.Product;
import com.dev_craft.catalogservice.service.ProductService;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/products")
@Slf4j
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public ResponseEntity<List<Product>> allProducts() {
        log.info("Finding all products");
        return ResponseEntity.ok(productService.findAllProducts());
    }

    @GetMapping("/{code}")
    public Product productByCode(@PathVariable String code) {
        log.info("Finding product by code :"+code);
        return productService.findProductByCode(code)
                .orElseThrow(() -> new NotFoundException("Product with code ["+code+"] doesn't exist"));
    }
}