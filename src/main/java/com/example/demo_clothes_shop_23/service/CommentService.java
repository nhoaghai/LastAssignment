package com.example.demo_clothes_shop_23.service;

import com.example.demo_clothes_shop_23.entities.*;
import com.example.demo_clothes_shop_23.exception.ResourceNotFoundException;
import com.example.demo_clothes_shop_23.repository.BlogRepository;
import com.example.demo_clothes_shop_23.repository.CommentRepository;
import com.example.demo_clothes_shop_23.request.UpsertCommentRequest;
import com.example.demo_clothes_shop_23.request.UpsertReviewRequest;
import com.example.demo_clothes_shop_23.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BlogRepository blogRepository;

    public List<Comment> getByBlog_IdOrderByCreatedAtDesc(Integer blogId) {
        try {
            return commentRepository.findByBlog_IdOrderByCreatedAtDesc(blogId);
        }catch (Exception e){
            e.printStackTrace();
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    //Sử dụng SecurityContextHolder để lấy user
    public Comment createComment(UpsertCommentRequest commentRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = (User) userDetails.getUser();

        //Kiểm tra xem blog có tồn tại hay không
        Blog blog = blogRepository.findById(commentRequest.getBlogId()).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài viết"));

        //Tạo review
        Comment comment = Comment.builder()
            .content(commentRequest.getContent())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .blog(blog)
            .user(user).build();
        commentRepository.save(comment);

        return comment;
    }

    //Sử dụng SecurityContextHolder để lấy user
    public Comment updateComment(UpsertCommentRequest commentRequest, Integer id) {
        //Kiểm tra comment xem tồn tại ko
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bình luận"));

        //Kiểm tra user này có tồn tại hay ko
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = (User) userDetails.getUser();

        //Kiểm tra xem blog có tồn tại hay không
        Blog blog = blogRepository.findById(commentRequest.getBlogId()).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài viết"));

        //Kiểm tra xem comment này có của user này ko
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Người dùng không sở hữu bình luận này");
        }

        if (!comment.getBlog().getId().equals(blog.getId())) {
            throw new RuntimeException("Không phải bình luận của bài viết này");
        }

        comment.setContent(commentRequest.getContent());
        comment.setUpdatedAt(LocalDateTime.now());
        commentRepository.save(comment);

        return comment;

    }


    //Sử dụng SecurityContextHolder để lấy user
    public void deleteComment(Integer id) {
        //Kiểm tra comment xem tồn tại ko
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        //Kiểm tra user này có tồn tại hay ko
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = (User) userDetails.getUser();

        //Kiểm tra xem review này có của user này ko
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Người dùng không sở hữu bình luận này");
        }

        commentRepository.delete(comment);
    }
}
