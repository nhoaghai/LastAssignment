package com.example.demo_clothes_shop_23.rest.adminApi;

import com.example.demo_clothes_shop_23.entities.Quantity;
import com.example.demo_clothes_shop_23.service.QuantityService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/quantities")
@AllArgsConstructor
public class AdminQuantityApi {
    private final QuantityService quantityService;

    @GetMapping("/product/{productId}/color/{colorId}/size/{sizeId}")
    public ResponseEntity<?> getQuantityByProductAndColorAndSize(@PathVariable int productId, @PathVariable int colorId, @PathVariable int sizeId) {
        Quantity quantity = quantityService.getByProductIdAndColorIdAndSizeId(productId, colorId, sizeId);
        return ResponseEntity.ok(quantity);
    }
}
