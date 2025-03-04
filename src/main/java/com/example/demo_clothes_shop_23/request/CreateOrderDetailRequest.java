package com.example.demo_clothes_shop_23.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateOrderDetailRequest {
    Integer orderId;
    Integer productId;
    Integer colorId;
    Integer sizeId;
    Integer quantity;
}
