package com.example.demo_clothes_shop_23.entities;

import com.example.demo_clothes_shop_23.model.enums.TokenType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "token_confirm")
public class TokenConfirm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String token;
    @Enumerated(EnumType.ORDINAL)
    TokenType type;
    LocalDateTime expiresAt;
    LocalDateTime confirmedAt;
    LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
