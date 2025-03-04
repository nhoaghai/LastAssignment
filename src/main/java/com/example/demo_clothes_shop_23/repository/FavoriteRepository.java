package com.example.demo_clothes_shop_23.repository;

import com.example.demo_clothes_shop_23.entities.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    @Query("SELECT DISTINCT f FROM Favorite f JOIN f.product p JOIN f.user u WHERE u.id = ?1 AND p.status=true ORDER BY f.createdAt DESC")
    List<Favorite> findByUser_IdOrderByCreatedAtDesc(Integer userId);

    @Query("SELECT DISTINCT f FROM Favorite f JOIN f.product p JOIN f.user u WHERE p.id = ?1 AND  u.id =?2 AND p.status=true ")
    Optional<Favorite> findByProduct_IdAndUser_Id(Integer productId, Integer id);
}
