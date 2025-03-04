package com.example.demo_clothes_shop_23.repository;

import com.example.demo_clothes_shop_23.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    public List<Cart> findByUser_IdOrderByCreatedAt(Integer userId);

}
