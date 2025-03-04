package com.example.demo_clothes_shop_23.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpsertTagRequest {
    @NotBlank(message = "K đc để trống name")
    String name;
}
