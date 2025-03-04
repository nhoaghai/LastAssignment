package com.example.demo_clothes_shop_23.repository;

import com.example.demo_clothes_shop_23.entities.Orders;
import com.example.demo_clothes_shop_23.model.enums.OrdersStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    Orders findByCodeOrder(String codeOrder);

    List<Orders> findByUser_IdOrderByCreatedAtDesc(Integer user_id);

    @Query("SELECT o FROM Orders o WHERE FUNCTION('MONTH', o.createdAt) = :currentMonth " +
        "AND FUNCTION('YEAR', o.createdAt) = :currentYear")
    List<Orders> findOrdersCreatedThisMonth(@Param("currentMonth") int currentMonth, @Param("currentYear") int currentYear);

    @Query("SELECT SUM(o.finalTotal) FROM Orders o WHERE FUNCTION('MONTH', o.createdAt) = :currentMonth " +
        "AND FUNCTION('YEAR', o.createdAt) = :currentYear " +
        "AND o.status = :status")
    Long findTotalFinalTotalCreatedThisMonth(@Param("currentMonth") int currentMonth,
                                             @Param("currentYear") int currentYear,
                                             @Param("status") OrdersStatus status);
}
