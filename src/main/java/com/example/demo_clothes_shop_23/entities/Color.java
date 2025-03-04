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
@Table(name = "colors")
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String colorName;
    String colorCode;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
