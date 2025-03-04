package com.example.demo_clothes_shop_23.service;

import com.example.demo_clothes_shop_23.entities.Category;
import com.example.demo_clothes_shop_23.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> getCategoriesWithNullParentId() {
        return categoryRepository.findCategoriesWithNullParentId();
    }

    public List<Category> getCategoriesWithParentId() {
        return categoryRepository.findCategoriesWithParentId();
    }

    public List<Category> getByParentId(Integer parentId) {
        return categoryRepository.findByParentId(parentId);
    }

}
