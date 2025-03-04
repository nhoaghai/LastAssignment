package com.example.demo_clothes_shop_23.entities;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "coupons")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String code;
    Integer amount;

    LocalDateTime startDate;
    LocalDateTime endDate;
    Boolean active;
    Integer quantity;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "coupon_user_ids", joinColumns = @JoinColumn(name = "coupon_id"))
    @Column(name = "user_id")
    List<Integer> listUserIdUsed;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
