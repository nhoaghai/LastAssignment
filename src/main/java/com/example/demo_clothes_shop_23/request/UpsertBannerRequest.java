package com.example.demo_clothes_shop_23.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpsertBannerRequest {
    @NotEmpty(message = "Không được để trống")
    String name;
    @NotEmpty(message = "Không được để trống")
    String link;
}
