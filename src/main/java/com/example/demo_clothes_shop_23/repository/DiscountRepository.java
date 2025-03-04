package com.example.demo_clothes_shop_23.repository;

import com.example.demo_clothes_shop_23.entities.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Integer> {
    List<Discount> findByActive(Boolean active);

    List<Discount> findByEndDateBefore(LocalDateTime now);
}
