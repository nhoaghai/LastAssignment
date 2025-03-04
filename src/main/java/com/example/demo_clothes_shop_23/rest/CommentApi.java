package com.example.demo_clothes_shop_23.rest;

import com.example.demo_clothes_shop_23.entities.Comment;
import com.example.demo_clothes_shop_23.request.UpsertCommentRequest;
import com.example.demo_clothes_shop_23.service.CommentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentApi {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> createComment(@Valid @RequestBody UpsertCommentRequest commentRequest) {
        Comment comment = commentService.createComment(commentRequest);
        return new ResponseEntity<>(comment, HttpStatus.CREATED); //201
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(@Valid @PathVariable Integer id, @Valid @RequestBody UpsertCommentRequest commentRequest) {
        Comment comment = commentService.updateComment(commentRequest, id);
        return ResponseEntity.ok(comment); //200
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Integer id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build(); //204
    }
}
