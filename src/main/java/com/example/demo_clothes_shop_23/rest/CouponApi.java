package com.example.demo_clothes_shop_23.rest;

import com.example.demo_clothes_shop_23.entities.Coupon;
import com.example.demo_clothes_shop_23.request.UseCouponRequest;
import com.example.demo_clothes_shop_23.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponApi {
    private final CouponService couponService;

    @GetMapping("/{userId}/{couponCode}")
    public ResponseEntity<?> getCoupon(@PathVariable Integer userId, @PathVariable String couponCode) {
        Coupon coupon = couponService.getCoupon(userId,couponCode);
        return ResponseEntity.ok(coupon);
    }
}
