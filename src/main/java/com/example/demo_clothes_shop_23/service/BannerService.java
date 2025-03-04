package com.example.demo_clothes_shop_23.service;

import com.example.demo_clothes_shop_23.entities.Banner;
import com.example.demo_clothes_shop_23.exception.ResourceNotFoundException;
import com.example.demo_clothes_shop_23.model.response.ImageResponse;
import com.example.demo_clothes_shop_23.repository.BannerRepository;
import com.example.demo_clothes_shop_23.request.UpsertBannerRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class BannerService {
    private final BannerRepository bannerRepository;
    private final FileServerService fileServerService;

    public List<Banner> getAll() {
        return bannerRepository.findAll();
    }

    public List<Banner> getBannerByStatus(Boolean status) {
        return bannerRepository.findByStatus(status);
    }

    public Banner getById(Integer id) {
        return bannerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Banner not found"));
    }

    public void createBanner(UpsertBannerRequest upsertBannerRequest) {
        Banner banner = Banner.builder()
            .name(upsertBannerRequest.getName())
            .url(upsertBannerRequest.getLink())
            .thumbnail("https://placehold.co/1920x800?text="+String.valueOf(upsertBannerRequest.getName().charAt(0)).toUpperCase())
            .status(false)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        bannerRepository.save(banner);
    }


    public void updateBanner(UpsertBannerRequest upsertBannerRequest, Integer id) {
        Banner banner = bannerRepository.findById(id).orElseThrow(
            ()-> new RuntimeException("Banner not found")
        );
        banner.setName(upsertBannerRequest.getName());
        banner.setUrl(upsertBannerRequest.getLink());
        banner.setUpdatedAt(LocalDateTime.now());
        bannerRepository.save(banner);
    }

    public String uploadThumbnail(Integer bannerId, MultipartFile file) {
        Banner banner = bannerRepository.findById(bannerId).orElseThrow(
            () -> new ResourceNotFoundException("Banner not found")
        );
        ImageResponse imageResponse = fileServerService.uploadFile(file);
        banner.setThumbnail(imageResponse.getUrl());
        bannerRepository.save(banner);
        return banner.getThumbnail();
    }

    public void updateStatus(List<Integer> selectedBannerId) {
        List<Banner> banners = bannerRepository.findAll();
        banners.forEach(banner->{
            banner.setStatus(selectedBannerId.contains(banner.getId()));
            bannerRepository.save(banner);
        });

    }
}
