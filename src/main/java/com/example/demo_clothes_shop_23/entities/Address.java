package com.example.demo_clothes_shop_23.entities;

import com.example.demo_clothes_shop_23.model.enums.UserRole;
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
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

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
    @Column(nullable = false, columnDefinition = "TEXT")
    String addressDetail;
    Boolean chosen;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
