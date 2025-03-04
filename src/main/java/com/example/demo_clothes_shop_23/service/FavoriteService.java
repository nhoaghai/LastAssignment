package com.example.demo_clothes_shop_23.service;

import com.example.demo_clothes_shop_23.entities.Favorite;
import com.example.demo_clothes_shop_23.entities.Product;
import com.example.demo_clothes_shop_23.entities.User;
import com.example.demo_clothes_shop_23.exception.ResourceNotFoundException;
import com.example.demo_clothes_shop_23.repository.FavoriteRepository;
import com.example.demo_clothes_shop_23.repository.ProductRepository;
import com.example.demo_clothes_shop_23.repository.UserRepository;
import com.example.demo_clothes_shop_23.request.FavoriteRequest;
import com.example.demo_clothes_shop_23.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;

    public List<Favorite> getByUser_IdOrderByCreatedAtDesc() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails customUserDetails) {
            User user = (User) customUserDetails.getUser();
           return favoriteRepository.findByUser_IdOrderByCreatedAtDesc(user.getId());
        }else return new ArrayList<>();
    }

    public Favorite getByProduct_Id(Integer productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails customUserDetails) {
            User user = (User) customUserDetails.getUser();
            return favoriteRepository.findByUser_IdOrderByCreatedAtDesc(user.getId())
                .stream()
                .filter(f -> Objects.equals(f.getProduct().getId(), productId))
                .findFirst()
                .orElse(null);
        }else return null;
    }

    //Sử dụng SecurityContextHolder để lấy user
    public Favorite createFavorite(FavoriteRequest favoriteRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = (User) userDetails.getUser();

        //Kiểm tra xem product có tồn tại hay không
        Product product = productRepository.findById(favoriteRequest.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm này"));

        //Tạo review
        Favorite favorite = Favorite.builder()
            .createdAt(LocalDateTime.now())
            .product(product)
            .user(user).build();
        favoriteRepository.save(favorite);
        return favorite;
    }

    //TODO: sử dụng SecurityContextHolder để lấy user
    public void deleteFavorite(Integer productId) {
        //Kiểm tra user này có tồn tại hay ko
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = (User) userDetails.getUser();

        //Kiểm tra review xem tồn tại ko
        Favorite favorite = favoriteRepository.findByProduct_IdAndUser_Id(productId,user.getId()).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm yêu thích này"));

        //Kiểm tra xem review này có của user này ko
        if (!favorite.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Người dùng không sở hữu sản phẩm yêu thích này");
        }

        favoriteRepository.delete(favorite);
    }
}
