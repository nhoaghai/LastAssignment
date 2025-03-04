package com.example.demo_clothes_shop_23.request;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class CreateCartRequest {
    private Integer userId;
    private Integer productId;
    private Integer colorId;
    private Integer sizeId;
    private Integer quantity;
}
