package com.example.demo_clothes_shop_23.entities;
import com.example.demo_clothes_shop_23.model.enums.ImageType;
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
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String imgUrl;
    @Enumerated(EnumType.ORDINAL)
    ImageType type;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;

    @ManyToOne
    @JoinColumn(name = "color_id")
    Color color;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
