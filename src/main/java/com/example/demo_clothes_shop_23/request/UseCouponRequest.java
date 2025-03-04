package com.example.demo_clothes_shop_23.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString@FieldDefaults(level = AccessLevel.PRIVATE)
public class UseCouponRequest {
    Integer userId;
    String couponCode;
}
