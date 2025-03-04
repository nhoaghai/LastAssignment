package com.example.demo_clothes_shop_23.rest.adminApi;

import com.example.demo_clothes_shop_23.entities.Blog;
import com.example.demo_clothes_shop_23.request.UpsertBlogRequest;
import com.example.demo_clothes_shop_23.service.BlogService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/blogs")
@AllArgsConstructor
public class AdminBlogApi {
    private final BlogService blogService;

    @PostMapping
    public ResponseEntity<?> createBlog(@Valid @RequestBody UpsertBlogRequest blogRequest) {
        Blog blog = blogService.createBlog(blogRequest);
        return new ResponseEntity<>(blog, HttpStatus.CREATED); //201
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBlog(@Valid @PathVariable Integer id, @Valid @RequestBody UpsertBlogRequest blogRequest) {
        Blog blog = blogService.updateBlog(blogRequest, id);
        return ResponseEntity.ok(blog); //200
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBlog(@PathVariable Integer id) {
        blogService.deleteBlog(id);
        return ResponseEntity.noContent().build(); //204
    }

    @PostMapping("/{id}/upload-thumbnail")
    public ResponseEntity<?> uploadThumbnail( @PathVariable Integer id, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(blogService.uploadThumbnail(id,file));
    }

    @GetMapping("/tags/{tagId}")
    public ResponseEntity<?> getBlogByTagId(@PathVariable Integer tagId) {
        List<Blog> blogs = blogService.getByTagId(tagId);
        return new ResponseEntity<>(blogs, HttpStatus.OK); //201
    }
}
