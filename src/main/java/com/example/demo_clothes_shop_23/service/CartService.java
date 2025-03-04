package com.example.demo_clothes_shop_23.service;

import com.example.demo_clothes_shop_23.entities.Cart;
import com.example.demo_clothes_shop_23.entities.Favorite;
import com.example.demo_clothes_shop_23.entities.Product;
import com.example.demo_clothes_shop_23.entities.User;
import com.example.demo_clothes_shop_23.exception.ResourceNotFoundException;
import com.example.demo_clothes_shop_23.repository.*;
import com.example.demo_clothes_shop_23.request.CreateCartRequest;
import com.example.demo_clothes_shop_23.request.FavoriteRequest;
import com.example.demo_clothes_shop_23.request.UpdateQuantityCartRequest;
import com.example.demo_clothes_shop_23.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;

    public List<Cart> getByUser_IdOrderByCreatedAt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = (User) userDetails.getUser();
        return cartRepository.findByUser_IdOrderByCreatedAt(user.getId());
    }

    //Sử dụng SecurityContextHolder để lấy user
    public Cart updateQuantityCart(UpdateQuantityCartRequest updateQuantityCartRequest) {
        //Kiểm tra xem product có tồn tại hay không
        Cart cart = cartRepository.findById(updateQuantityCartRequest.getCartId()).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giỏ hàng"));

        cart.setQuantity(updateQuantityCartRequest.getQuantity());
        cartRepository.save(cart);
        return cart;
    }

    public void deleteCart(Integer id) {
        Cart cart = cartRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giỏ hàng"));
        cartRepository.delete(cart);
    }

    public Cart createCart(CreateCartRequest createCartRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = (User) userDetails.getUser();

        //Kiểm tra xem product có tồn tại hay không
        Product product = productRepository.findById(createCartRequest.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm"));

        List<Cart> cartList = cartRepository.findByUser_IdOrderByCreatedAt(user.getId());
        Optional<Cart> existingCartItem = cartList.stream()
            .filter(cart -> cart.getProduct().getId().equals(createCartRequest.getProductId())
                && cart.getColor().getId().equals(createCartRequest.getColorId())
                && cart.getSize().getId().equals(createCartRequest.getSizeId()))
            .findFirst();

        Cart cart;
        if (existingCartItem.isPresent()) {
            // Nếu sản phẩm đã có trong giỏ hàng, cập nhật số lượng
            cart = existingCartItem.get();
            cart.setQuantity(cart.getQuantity() + createCartRequest.getQuantity());
            cart.setUpdatedAt(LocalDateTime.now());
        } else {
            // Nếu sản phẩm chưa có trong giỏ hàng, tạo mới
            cart = Cart.builder()
                .user(user)
                .product(product)
                .color(colorRepository.findById(createCartRequest.getColorId()).orElseThrow())
                .size(sizeRepository.findById(createCartRequest.getSizeId()).orElseThrow())
                .quantity(createCartRequest.getQuantity())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        }
        // Lưu giỏ hàng (hoặc cập nhật nếu đã tồn tại)
        cartRepository.save(cart);
        return cart;
    }
}
