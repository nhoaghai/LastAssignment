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
@Table(name = "blogs")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false)
    String title;
    String slug;
    @Column(columnDefinition = "TEXT")
    String description;
    @Column(nullable = false, columnDefinition = "TEXT")
    String content;
    String thumbnail;
    Boolean status;
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToMany
    @JoinTable(
            name = "blog_tag",
            joinColumns = @JoinColumn(name = "blog_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    List<Tag> tags;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
