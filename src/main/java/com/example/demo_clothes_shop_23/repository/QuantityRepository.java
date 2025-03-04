package com.example.demo_clothes_shop_23.repository;

import com.example.demo_clothes_shop_23.entities.Quantity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuantityRepository extends JpaRepository<Quantity, Integer> {
    Quantity findByProductIdAndColorIdAndSizeId(Integer product_id, Integer color_id, Integer size_id);

    List<Quantity> findByProductId(Integer product_id);
}
