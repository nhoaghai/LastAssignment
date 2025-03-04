package com.example.demo_clothes_shop_23.rest.adminApi;

import com.example.demo_clothes_shop_23.entities.Image;
import com.example.demo_clothes_shop_23.request.CreateSubImageRequest;
import com.example.demo_clothes_shop_23.request.UpsertProductRequest;
import com.example.demo_clothes_shop_23.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/products")
@AllArgsConstructor
public class AdminProductApi {
    private ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody UpsertProductRequest upsertProductRequest) {
        productService.createProduct(upsertProductRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer productId, @RequestBody UpsertProductRequest upsertProductRequest) {
        Map<Integer, List<Image>> imageMap = productService.updateProduct(productId,upsertProductRequest);
        return ResponseEntity.ok().body(imageMap);
    }

    @PostMapping("/{productId}/update-poster")
    public ResponseEntity<?> updatePoster(@RequestParam("file") MultipartFile file, @PathVariable Integer productId) {
        return ResponseEntity.ok(productService.updatePoster(productId, file));
    }

    @PostMapping("/images/{mainImageId}/upload-main")
    public ResponseEntity<?> uploadMainImage(@PathVariable Integer mainImageId, @RequestParam("file") MultipartFile file) {
        Image mainImage = productService.updateMainImage(mainImageId,file);
        return ResponseEntity.ok().body(mainImage);
    }

    @PostMapping("/images/upload-sub")
    public ResponseEntity<?> uploadSubImage(@RequestParam("file") MultipartFile file, @RequestParam("productId") Integer productId, @RequestParam("colorId") Integer colorId) {
        CreateSubImageRequest createSubImageRequest = new CreateSubImageRequest(file, productId, colorId);
        Image subImage = productService.createSubImage(createSubImageRequest);
        return ResponseEntity.ok().body(subImage);
    }

    @DeleteMapping("/images/{imageId}")
    public void deleteImage(@PathVariable Integer imageId){
        productService.deleteImage(imageId);
    }
}
