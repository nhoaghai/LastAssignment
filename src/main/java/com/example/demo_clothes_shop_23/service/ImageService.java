package com.example.demo_clothes_shop_23.service;

import com.example.demo_clothes_shop_23.decorator.ImageProductDetailDecorator;
import com.example.demo_clothes_shop_23.entities.Image;
import com.example.demo_clothes_shop_23.model.model.ImageProductDetailModel;
import com.example.demo_clothes_shop_23.repository.ImageRepository;
import com.example.demo_clothes_shop_23.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final ImageProductDetailDecorator imageProductDetailDecorator;

    public List<ImageProductDetailModel> getAllByColor_IdAndProduct_Id(Integer colorId, Integer productId) {
        List<Image> images= imageRepository.findAllByColor_IdAndProduct_Id(colorId, productId);
        return imageProductDetailDecorator.decorate(images);
    }

    public List<Image> getByProductId(int id) {
        return imageRepository.findByProduct_Id(id);
    }
}
