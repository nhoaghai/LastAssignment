package com.example.demo_clothes_shop_23.rest;

import com.example.demo_clothes_shop_23.entities.Orders;
import com.example.demo_clothes_shop_23.entities.OrdersDetail;
import com.example.demo_clothes_shop_23.request.CreateOrderDetailRequest;
import com.example.demo_clothes_shop_23.request.CreateOrderRequest;
import com.example.demo_clothes_shop_23.service.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orderDetails")
@RequiredArgsConstructor
public class OrderDetailApi {
    private final OrderDetailService orderDetailService;

    @PutMapping
    public ResponseEntity<?> createOrderDetail(@Valid @RequestBody CreateOrderDetailRequest createOrderDetailRequest) {
        OrdersDetail ordersDetail = orderDetailService.createOrderDetail(createOrderDetailRequest);
        return new ResponseEntity<>(ordersDetail, HttpStatus.CREATED);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getByOrderId(@PathVariable Integer orderId){
        List<OrdersDetail> ordersDetails = orderDetailService.getByOrderId(orderId);
        return new ResponseEntity<>(ordersDetails,HttpStatus.OK);
    }
}
