package com.example.demo_clothes_shop_23.rest.adminApi;

import com.example.demo_clothes_shop_23.entities.Tag;
import com.example.demo_clothes_shop_23.request.UpsertTagRequest;
import com.example.demo_clothes_shop_23.service.TagService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/tags")
@AllArgsConstructor
public class AdminTagApi {
    private final TagService tagService;

    @PostMapping("/create")
    public ResponseEntity<?> createTag(@RequestBody UpsertTagRequest upsertTagRequest) {
        Tag tag = tagService.createTag(upsertTagRequest);
        return ResponseEntity.ok(tag);
    }

    @PutMapping("/update/{tagId}")
    public ResponseEntity<?> updateTag(@RequestBody UpsertTagRequest upsertTagRequest, @PathVariable Integer tagId) {
        Tag tag = tagService.updateTag(upsertTagRequest,tagId);
        return ResponseEntity.ok(tag);
    }

    @DeleteMapping("/delete/{tagId}")
    public void deleteTag(@PathVariable Integer tagId) {
      tagService.deleteTag(tagId);
    }
}
