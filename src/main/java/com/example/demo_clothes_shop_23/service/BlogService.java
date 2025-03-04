package com.example.demo_clothes_shop_23.service;

import com.example.demo_clothes_shop_23.entities.Blog;
import com.example.demo_clothes_shop_23.entities.Comment;
import com.example.demo_clothes_shop_23.entities.Tag;
import com.example.demo_clothes_shop_23.entities.User;
import com.example.demo_clothes_shop_23.exception.ResourceNotFoundException;
import com.example.demo_clothes_shop_23.model.response.ImageResponse;
import com.example.demo_clothes_shop_23.repository.BlogRepository;
import com.example.demo_clothes_shop_23.repository.CommentRepository;
import com.example.demo_clothes_shop_23.request.UpsertBlogRequest;
import com.example.demo_clothes_shop_23.security.CustomUserDetails;
import com.github.slugify.Slugify;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;
    private final TagService tagService;
    private final CommentRepository commentRepository;
    private final FileServerService fileServerService;

    public List<Blog> getAll() {
        return blogRepository.findAll();
    }

    public List<Blog> getAllByUserIdOrderByCreatedAtDesc() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = (User) userDetails.getUser();
        return blogRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId());
    }

    public Blog getBlogById(int id) {
        return blogRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("No blog found with id: " + id)
        );
    }

    public Page<Blog> getByStatusOrderByCreatedAt(Boolean status, int page, int pageSize){
        PageRequest pageRequest = PageRequest.of(page-1, pageSize, Sort.by("createdAt").descending());
        return blogRepository.findByStatusOrderByCreatedAt(status, pageRequest);
    }

    public Blog getByIdAndSlugAndStatus(Integer id, String slug, Boolean status){
        return blogRepository.findByIdAndSlugAndStatus(id, slug, status);
    }

    public Page<Blog> getByTagIdAndStatus(Integer tagId, Boolean status, int page, int pageSize){
        PageRequest pageRequest = PageRequest.of(page-1, pageSize, Sort.by("createdAt").descending());
        return blogRepository.findByTagIdAndStatus(tagId, status, pageRequest);
    }

    public List<Blog> getByTagIdAndStatusOrderByCreatedAtDesc(Integer tagId, Boolean status){
        return blogRepository.findByTagIdAndStatusOrderByCreatedAtDesc(tagId, status)
            .stream()
            .limit(3)
            .toList();
    }

    public List<Blog> getBlogsCreatedThisMonth() {
        LocalDateTime now = LocalDateTime.now();
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();
        return blogRepository.findBlogsCreatedThisMonth(currentMonth, currentYear);
    }


    public Blog createBlog(UpsertBlogRequest blogRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = (User) userDetails.getUser();

        Slugify slugify= Slugify.builder().build();
        Blog blog = Blog.builder()
            .title(blogRequest.getTitle())
            .slug(slugify.slugify(blogRequest.getTitle()))
            .content(blogRequest.getContent())
            .description(blogRequest.getDescription())
            .status(blogRequest.getStatus())
            .tags(tagService.getAllByIds(blogRequest.getTagIds()))
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .user(user)
            .build();
        blogRepository.save(blog);
        return blog;
    }


    public Blog updateBlog(UpsertBlogRequest blogRequest, Integer id) {
        //Kiểm tra blog có tồn tại hay không
        Blog blog = blogRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Blog not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = (User) userDetails.getUser();

        //Kiểm tra xem blog này có thuộc về user này không
        if (!blog.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("User not authorized to update blog");
        }
        Slugify slugify= Slugify.builder().build();
        blog.setTitle(blogRequest.getTitle());
        blog.setSlug(slugify.slugify(blog.getTitle()));
        blog.setContent(blogRequest.getContent());
        blog.setDescription(blogRequest.getDescription());
        blog.setStatus(blogRequest.getStatus());
        blog.setTags(tagService.getAllByIds(blogRequest.getTagIds()));
        blog.setUpdatedAt(LocalDateTime.now());
        blogRepository.save(blog);
        return blog;
    }

    public void deleteBlog(Integer id) {
        //Kiểm tra blog có tồn tại hay không
        Blog blog = blogRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Blog not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = (User) userDetails.getUser();

        //Kiểm tra xem blog này có thuộc về user này không
        if (!blog.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("User not authorized to update blog");
        }

        List<Comment> comments = commentRepository.findByBlog_IdOrderByCreatedAtDesc(blog.getId());
        commentRepository.deleteAll(comments);

        blogRepository.delete(blog);
    }

    public String uploadThumbnail(Integer id, MultipartFile file) {
        Blog blog = blogRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Blog not found"));
        ImageResponse imageResponse = fileServerService.uploadFile(file);
        blog.setThumbnail(imageResponse.getUrl());
        blogRepository.save(blog);
        return imageResponse.getUrl();
    }

    public List<Blog> getByTagId(Integer tagId) {
        return blogRepository.findByTagId(tagId);
    }
}
