package com.example.demo_clothes_shop_23.service;

import com.example.demo_clothes_shop_23.entities.Product;
import com.example.demo_clothes_shop_23.entities.Review;
import com.example.demo_clothes_shop_23.entities.User;
import com.example.demo_clothes_shop_23.exception.ResourceNotFoundException;
import com.example.demo_clothes_shop_23.repository.ProductRepository;
import com.example.demo_clothes_shop_23.repository.ReviewRepository;
import com.example.demo_clothes_shop_23.request.UpsertReviewRequest;
import com.example.demo_clothes_shop_23.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    public List<Review> findByProductId(Integer productId) {
        return reviewRepository.findByProductId(productId);
    }

    public List<Review> findByProduct_IdOrderByCreatedAtDesc(int productId) {
        return reviewRepository.findByProduct_IdOrderByCreatedAtDesc(productId);
    }

    //Sử dụng SecurityContextHolder để lấy user
    public Review createReview(UpsertReviewRequest reviewRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = (User) userDetails.getUser();

        //Kiểm tra xem product có tồn tại hay không
        Product product = productRepository.findById(reviewRequest.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        //Tạo review
        Review review = Review.builder()
            .content(reviewRequest.getContent())
            .rating(reviewRequest.getRating())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .product(product)
            .user(user).build();
        reviewRepository.save(review);

        List<Review> reviewsOfThisProduct = reviewRepository.findByProduct_IdOrderByCreatedAtDesc(product.getId());
        // Kiểm tra xem có đánh giá nào không
        if (!reviewsOfThisProduct.isEmpty()) {
            // Tính tổng số điểm đánh giá
            double totalRating = 0.0;
            for (Review rv : reviewsOfThisProduct) {
                totalRating += (double)rv.getRating();
            }
            // Tính điểm đánh giá trung bình
            double productRating = totalRating / reviewsOfThisProduct.size();
            product.setRating(productRating);
            productRepository.save(product);
        } else {
            // Nếu không có đánh giá nào, có thể đặt rating mặc định (ví dụ: 0 hoặc giá trị khác)
            product.setRating(0.0);
            productRepository.save(product);
        }

        return review;
    }

    //Sử dụng SecurityContextHolder để lấy user
    public Review updateReview(UpsertReviewRequest reviewRequest, Integer id) {
        //Kiểm tra review xem tồn tại ko
        Review review = reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đánh giá này"));

        //Kiểm tra user này có tồn tại hay ko
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = (User) userDetails.getUser();

        //Kiểm tra xem product có tồn tại hay không
        Product product = productRepository.findById(reviewRequest.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm này"));

        //Kiểm tra xem review này có của user này ko
        if (!review.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Người dùng không sở hữu đánh giá này");
        }

        //Kểm tra xem review này có thuộc movie hay k
        if (!review.getProduct().getId().equals(product.getId())) {
            throw new RuntimeException("Đánh giá không thuộc sản phẩm này");
        }

        review.setContent(reviewRequest.getContent());
        review.setRating(reviewRequest.getRating());
        review.setUpdatedAt(LocalDateTime.now());
        reviewRepository.save(review);

        List<Review> reviewsOfThisProduct = reviewRepository.findByProduct_IdOrderByCreatedAtDesc(product.getId());
        // Kiểm tra xem có đánh giá nào không
        if (!reviewsOfThisProduct.isEmpty()) {
            // Tính tổng số điểm đánh giá
            double totalRating = 0.0;
            for (Review rv : reviewsOfThisProduct) {
                totalRating += (double)rv.getRating();
            }
            // Tính điểm đánh giá trung bình
            double productRating = totalRating / reviewsOfThisProduct.size();
            product.setRating(productRating);
            productRepository.save(product);
        } else {
            // Nếu không có đánh giá nào, có thể đặt rating mặc định (ví dụ: 0 hoặc giá trị khác)
            product.setRating(0.0);
            productRepository.save(product);
        }

        return review;

    }

    //Sử dụng SecurityContextHolder để lấy user
    public void deleteReview(Integer productId, Integer id) {
        //Kiểm tra review xem tồn tại ko
        Review review = reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đánh giá này"));

        //Kiểm tra user này có tồn tại hay ko
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = (User) userDetails.getUser();

        //Kiểm tra xem review này có của user này ko
        if (!review.getUser().getId().equals(user.getId()) && !Objects.equals(user.getRole().toString(), "ADMIN")) {
            throw new RuntimeException("Người dùng không sở hữu đánh giá này");
        }

        List<Review> reviewsOfThisProduct = reviewRepository.findByProduct_IdOrderByCreatedAtDesc(productId);
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm này"));
        // Kiểm tra xem có đánh giá nào không
        if (!reviewsOfThisProduct.isEmpty()) {
            // Tính tổng số điểm đánh giá
            double totalRating = 0.0;
            for (Review rv : reviewsOfThisProduct) {
                totalRating +=(double) rv.getRating();
            }
            // Tính điểm đánh giá trung bình
            double productRating = totalRating / reviewsOfThisProduct.size();
            product.setRating(productRating);
            productRepository.save(product);
        } else {
            // Nếu không có đánh giá nào, có thể đặt rating mặc định (ví dụ: 0 hoặc giá trị khác)
            product.setRating(0.0);
            productRepository.save(product);
        }

        reviewRepository.delete(review);
    }
}
