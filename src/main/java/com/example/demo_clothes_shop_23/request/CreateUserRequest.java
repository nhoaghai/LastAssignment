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
public class CreateUserRequest {
    @NotEmpty(message = "K được để trống")
    String name;
    @NotEmpty(message = "K được để trống")
    String email;
    @NotEmpty(message = "K được để trống")
    String password;
    @NotEmpty(message = "K được để trống")
    String roleString;
}
