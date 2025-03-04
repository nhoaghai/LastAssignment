package com.example.demo_clothes_shop_23.repository;

import com.example.demo_clothes_shop_23.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    List<Image> findAllByColor_IdAndProduct_Id(Integer colorId, Integer productId);

    List<Image> findByProduct_Id(Integer productId);

}
