package com.example.demo_clothes_shop_23.decorator;

import com.example.demo_clothes_shop_23.entities.Image;
import com.example.demo_clothes_shop_23.model.model.ImageProductDetailModel;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@AllArgsConstructor
public class ImageProductDetailDecorator {

    public List<ImageProductDetailModel> decorate(List<Image> images) {
        List<ImageProductDetailModel> imageProductDetailModels = new ArrayList<>();
        images.forEach(img -> {
            ImageProductDetailModel imageProductDetailModel = ImageProductDetailModel.builder()
                .id(img.getId())
                .imgUrl(img.getImgUrl())
                .build();
            imageProductDetailModels.add(imageProductDetailModel);
        });
        return imageProductDetailModels;
    }
}
