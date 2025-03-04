package com.example.demo_clothes_shop_23.entities;

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
@Table(name = "cost")
public class Cost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String name;

    @Column(columnDefinition = "TEXT")
    String description;

    Long amount;

    @OneToOne
    @JoinColumn(name = "user_id")
    User user;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
