package com.example.demo_clothes_shop_23.repository;

import com.example.demo_clothes_shop_23.entities.User;
import com.example.demo_clothes_shop_23.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByRole(UserRole userRole);

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE FUNCTION('MONTH', u.createdAt) = :currentMonth " +
        "AND FUNCTION('YEAR', u.createdAt) = :currentYear")
    List<User> findUsersCreatedThisMonth(@Param("currentMonth") int currentMonth, @Param("currentYear") int currentYear);

}

