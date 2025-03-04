package com.example.demo_clothes_shop_23.rest;

import com.example.demo_clothes_shop_23.entities.Orders;
import com.example.demo_clothes_shop_23.request.CreateOrderRequest;
import com.example.demo_clothes_shop_23.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderApi {
    private final OrderService orderService;

    @PutMapping("")
    public ResponseEntity<?> createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
        Orders order = orderService.createOrder(createOrderRequest);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @PutMapping("/updateCodeOrder/{orderId}")
    public ResponseEntity<?> updateCodeOrder(@PathVariable Integer orderId){
        Orders order = orderService.updateCodeOrder(orderId);
        return new ResponseEntity<>(order,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Integer id){
        Orders orders = orderService.getById(id);
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }

    @PutMapping("/cancelOrder/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable Integer orderId){
        Orders orders = orderService.cancelOrder(orderId);
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }
}
