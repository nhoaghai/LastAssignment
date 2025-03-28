package com.example.demo_clothes_shop_23.repository;

import com.example.demo_clothes_shop_23.entities.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Integer> {
    List<Banner> findByStatus(Boolean status);
}
