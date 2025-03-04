package com.example.demo_clothes_shop_23.repository;

import com.example.demo_clothes_shop_23.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    @Query("SELECT ROUND(AVG(r.rating), 1) FROM Review r WHERE r.product.id = :productId")
    Double findAverageRatingByProductId(@Param("productId") Integer productId);

    List<Review> findByProductId(Integer productId);

    List<Review> findByProduct_IdOrderByCreatedAtDesc(Integer productId);
}
