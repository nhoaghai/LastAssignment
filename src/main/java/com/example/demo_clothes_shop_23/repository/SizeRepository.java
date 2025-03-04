package com.example.demo_clothes_shop_23.repository;

import com.example.demo_clothes_shop_23.entities.Size;
import com.example.demo_clothes_shop_23.model.enums.SizeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface SizeRepository extends JpaRepository<Size, Integer> {
    Set<Size> findSizeByTypeOrderByOrdersAsc(SizeType type);

}
