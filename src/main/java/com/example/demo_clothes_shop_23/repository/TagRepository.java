package com.example.demo_clothes_shop_23.repository;

import com.example.demo_clothes_shop_23.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer> {
}
