package com.example.demo_clothes_shop_23.repository;

import com.example.demo_clothes_shop_23.entities.Cost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CostRepository extends JpaRepository<Cost, Integer> {

    @Query("SELECT SUM(c.amount) FROM Cost c WHERE FUNCTION('MONTH', c.createdAt) = :currentMonth " +
        "AND FUNCTION('YEAR', c.createdAt) = :currentYear")
    Long findCostsCreatedThisMonth(@Param("currentMonth") int currentMonth, @Param("currentYear") int currentYear);
}
