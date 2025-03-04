package com.example.demo_clothes_shop_23.entities;
import com.example.demo_clothes_shop_23.model.enums.SizeType;
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
@Table(name = "sizes")
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String sizeName;
    Integer orders;
    @Enumerated(EnumType.ORDINAL)
    SizeType type;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
