package com.example.demo_clothes_shop_23.rest;

import com.example.demo_clothes_shop_23.entities.Cart;
import com.example.demo_clothes_shop_23.request.CreateCartRequest;
import com.example.demo_clothes_shop_23.request.UpdateQuantityCartRequest;
import com.example.demo_clothes_shop_23.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartApi {
    private final CartService cartService;

    @PostMapping("/update-quantity-cart")
    public ResponseEntity<?> updateQuantityCart(@Valid @RequestBody UpdateQuantityCartRequest updateQuantityCartRequest) {
        Cart cart = cartService.updateQuantityCart(updateQuantityCartRequest);
        return new ResponseEntity<>(cart, HttpStatus.OK); //201
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCart(@PathVariable Integer id) {
        cartService.deleteCart(id);
        return ResponseEntity.noContent().build(); //204
    }

    @PutMapping("")
    public ResponseEntity<?> createCart(@Valid @RequestBody CreateCartRequest createCartRequest) {
        Cart cart = cartService.createCart(createCartRequest);
        return new ResponseEntity<>(cart, HttpStatus.CREATED); //201
    }
}
