package com.example.demo_clothes_shop_23.entities;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(nullable = false)
    String name;
    String slug;
    @Column(columnDefinition = "TEXT")
    String description;
    Long price;
    Boolean status;
    Double rating;
    String poster;
    Long newPrice;

    @ManyToOne
    @JoinColumn(name = "discount_id")
    Discount discount;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "product_color",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "color_id")
    )
    Set<Color> colors;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "product_size",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "size_id")
    )
    Set<Size> sizes;


    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
