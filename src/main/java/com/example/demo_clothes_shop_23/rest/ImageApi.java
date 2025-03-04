package com.example.demo_clothes_shop_23.rest;

import com.example.demo_clothes_shop_23.entities.Image;
import com.example.demo_clothes_shop_23.model.model.ImageProductDetailModel;
import com.example.demo_clothes_shop_23.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/images")
public class ImageApi {
    @Autowired
    private ImageService imageService;

    @GetMapping("/{colorId}/{productId}")
    public ResponseEntity<?> getAllByColorIdAndProductId(@PathVariable int colorId, @PathVariable int productId) {
        List<ImageProductDetailModel> images = imageService.getAllByColor_IdAndProduct_Id(colorId, productId);
        return ResponseEntity.ok(images);
    }
}
