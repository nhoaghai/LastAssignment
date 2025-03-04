package com.example.demo_clothes_shop_23.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FavoriteRequest {
    @NotNull(message = "không được để trống")
    Integer userId;
    @NotNull(message = "không được để trống")
    Integer productId;
}
