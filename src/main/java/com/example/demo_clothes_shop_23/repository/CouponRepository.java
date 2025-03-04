package com.example.demo_clothes_shop_23.repository;

import com.example.demo_clothes_shop_23.entities.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {
    Coupon findByCodeAndActive(String code,Boolean active);

    List<Coupon> findByActive(Boolean active);
}
