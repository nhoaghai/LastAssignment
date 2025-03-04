package com.example.demo_clothes_shop_23.repository;

import com.example.demo_clothes_shop_23.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByParentId(Integer parentId);

    @Query("SELECT c FROM Category c WHERE c.parentId IS NULL")
    List<Category> findCategoriesWithNullParentId();

    @Query("SELECT c FROM Category c WHERE c.parentId IS NOT NULL")
    List<Category> findCategoriesWithParentId();

}
