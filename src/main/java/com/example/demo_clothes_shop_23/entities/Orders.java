package com.example.demo_clothes_shop_23.entities;

import com.example.demo_clothes_shop_23.model.enums.DeliveryType;
import com.example.demo_clothes_shop_23.model.enums.OrdersStatus;
import com.example.demo_clothes_shop_23.model.enums.PaymentType;
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
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(unique = true)
    String codeOrder;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Column(nullable = false)
    String email;

    @Column(nullable = false)
    String receiverName;

    @Column(nullable = false)
    String phone;

    @Column(nullable = false)
    String province;
    @Column(nullable = false)
    String district;
    @Column(nullable = false)
    String ward;
    @Column(columnDefinition = "TEXT")
    String addressDetail;

    @Column(nullable = false)
    Long totalPrice;

    Long discountAmount;
    @Column(nullable = false)
    Long finalTotal;

    @Enumerated(EnumType.ORDINAL)
    OrdersStatus status;
    @Enumerated(EnumType.ORDINAL)
    PaymentType payment;
    @Enumerated(EnumType.ORDINAL)
    DeliveryType delivery;

    @Column(columnDefinition = "TEXT")
    String notes;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;

}
