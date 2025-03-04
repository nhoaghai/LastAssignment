package com.example.demo_clothes_shop_23.rest.adminApi;

import com.example.demo_clothes_shop_23.entities.Quantity;
import com.example.demo_clothes_shop_23.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/orders")
@AllArgsConstructor
public class AdminOrderApi {
    private final OrderService orderService;

    @PutMapping("{orderId}/update/{status}")
    public ResponseEntity<?> updateOrderStatus (@PathVariable String status, @PathVariable Integer orderId) {
        orderService.updateStatus(status,orderId);
        return ResponseEntity.ok().build();
    }


}
