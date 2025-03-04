package com.example.demo_clothes_shop_23.rest.adminApi;

import com.example.demo_clothes_shop_23.request.UpsertBannerRequest;
import com.example.demo_clothes_shop_23.service.BannerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/banners")
@AllArgsConstructor
public class AdminBannerApi {
    private final BannerService bannerService;

    @PutMapping("/create")
    public ResponseEntity<?> createBanner(@RequestBody UpsertBannerRequest upsertBannerRequest) {
        bannerService.createBanner(upsertBannerRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateBanner(@RequestBody UpsertBannerRequest upsertBannerRequest, @PathVariable Integer id) {
        bannerService.updateBanner(upsertBannerRequest,id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{bannerId}/upload-thumbnail")
    public ResponseEntity<?> uploadThumbnail( @PathVariable Integer bannerId, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(bannerService.uploadThumbnail(bannerId,file));
    }

    @PutMapping("/update-status")
    public ResponseEntity<?> updateStatus(@RequestBody List<Integer> selectedBannerId) {
        bannerService.updateStatus(selectedBannerId);
        return ResponseEntity.ok().build();
    }

}
