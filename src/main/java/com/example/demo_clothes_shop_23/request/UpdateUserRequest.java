package com.example.demo_clothes_shop_23.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserRequest {
    @NotEmpty(message = "K được để trống")
    String name;
    @NotEmpty(message = "K được để trống")
    String roleString;
    @NotEmpty(message = "K được để trống")
    Boolean enabled;
}
