package com.example.demo_clothes_shop_23.repository;

import com.example.demo_clothes_shop_23.entities.Color;
import com.example.demo_clothes_shop_23.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ColorRepository extends JpaRepository<Color, Integer> {
}
