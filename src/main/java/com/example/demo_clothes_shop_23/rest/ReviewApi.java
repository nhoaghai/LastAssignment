package com.example.demo_clothes_shop_23.rest;

import com.example.demo_clothes_shop_23.entities.Review;
import com.example.demo_clothes_shop_23.request.UpsertReviewRequest;
import com.example.demo_clothes_shop_23.service.ReviewService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@AllArgsConstructor
public class ReviewApi {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<?> createReview(@Valid @RequestBody UpsertReviewRequest reviewRequest) {
        Review review = reviewService.createReview(reviewRequest);
        return new ResponseEntity<>(review, HttpStatus.CREATED); //201
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReview(@Valid @PathVariable Integer id, @Valid @RequestBody UpsertReviewRequest reviewRequest) {
        Review review = reviewService.updateReview(reviewRequest, id);
        return ResponseEntity.ok(review); //200
    }

    @DeleteMapping("/{productId}/{id}")
    public ResponseEntity<?> deleteReview( @PathVariable Integer productId,@PathVariable Integer id) {
        reviewService.deleteReview(productId,id);
        return ResponseEntity.noContent().build(); //204
    }

}
