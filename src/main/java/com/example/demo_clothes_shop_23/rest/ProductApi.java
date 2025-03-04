package com.example.demo_clothes_shop_23.rest;

import com.example.demo_clothes_shop_23.entities.Product;

import com.example.demo_clothes_shop_23.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductApi {
    @Autowired
    private ProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<?> getById(@PathVariable int productId) {
        Product product = productService.getById(productId);
        return ResponseEntity.ok(product);
    }
}
