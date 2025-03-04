package com.example.demo_clothes_shop_23.vnPay.rest;

import com.example.demo_clothes_shop_23.vnPay.response.PaymentResponse;
import com.example.demo_clothes_shop_23.vnPay.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/payments")
@AllArgsConstructor
public class PaymentApi {
    private final PaymentService paymentService;

    @GetMapping("/create_payment/{orderId}")
    public ResponseEntity<?> createPayment(@PathVariable Integer orderId, HttpServletRequest request) throws UnsupportedEncodingException {
        PaymentResponse paymentResponse = paymentService.createPaymentResponse(orderId,request);
        return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
    }
}
