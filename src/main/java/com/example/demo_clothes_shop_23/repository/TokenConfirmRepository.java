package com.example.demo_clothes_shop_23.repository;

import com.example.demo_clothes_shop_23.entities.TokenConfirm;
import com.example.demo_clothes_shop_23.model.enums.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TokenConfirmRepository extends JpaRepository<TokenConfirm, Integer> {
    Optional<TokenConfirm> findByTokenAndType(String token, TokenType tokenType);
}
