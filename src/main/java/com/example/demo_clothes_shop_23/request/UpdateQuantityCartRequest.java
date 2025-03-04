package com.example.demo_clothes_shop_23.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateQuantityCartRequest {
    Integer quantity;
    Integer cartId;
}
