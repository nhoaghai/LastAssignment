package com.example.demo_clothes_shop_23.rest.adminApi;

import com.example.demo_clothes_shop_23.entities.Coupon;
import com.example.demo_clothes_shop_23.request.UpsertCouponRequest;
import com.example.demo_clothes_shop_23.service.CouponService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/coupons")
@AllArgsConstructor
public class AdminCouponApi {
    private final CouponService couponService;

    @PostMapping("/create")
    public ResponseEntity<?> createCoupon(@RequestBody UpsertCouponRequest upsertCouponRequest) {
        Coupon coupon = couponService.createCoupon(upsertCouponRequest);
        return ResponseEntity.ok(coupon);
    }

    @PutMapping("/update/{couponId}")
    public ResponseEntity<?> updateCoupon(@RequestBody UpsertCouponRequest upsertCouponRequest, @PathVariable Integer couponId) {
        Coupon coupon = couponService.updateCoupon(upsertCouponRequest,couponId);
        return ResponseEntity.ok(coupon);
    }

    @DeleteMapping("/delete/{couponId}")
    public void deleteCoupon(@PathVariable Integer couponId) {
        couponService.deleteCoupon(couponId);
    }
}
