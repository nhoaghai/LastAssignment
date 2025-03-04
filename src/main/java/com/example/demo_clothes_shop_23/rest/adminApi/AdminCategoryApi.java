package com.example.demo_clothes_shop_23.rest.adminApi;

import com.example.demo_clothes_shop_23.entities.Category;
import com.example.demo_clothes_shop_23.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
@AllArgsConstructor
public class AdminCategoryApi {
    private final CategoryService categoryService;

    @GetMapping("/categoryParent/{categoryParentId}")
    public ResponseEntity<?> getCategoryParent(@PathVariable Integer categoryParentId) {
        List<Category> categoryChild = categoryService.getByParentId(categoryParentId);
        return ResponseEntity.ok(categoryChild);
    }
}
