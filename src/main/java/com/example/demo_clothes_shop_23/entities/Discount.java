package com.example.demo_clothes_shop_23.entities;

import com.example.demo_clothes_shop_23.model.enums.DiscountType;
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
@Table(name = "discounts")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String name;
    @Column(columnDefinition = "TEXT")
    String description;

    @Enumerated(EnumType.ORDINAL)
    DiscountType type;
    Long amount;

    LocalDateTime startDate;
    LocalDateTime endDate;
    Boolean active;
    String imageUrl;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;

}
