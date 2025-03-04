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
public class ChangePasswordRequest {
    @NotEmpty(message = "K được để trống")
    String tokenString;

    @NotEmpty(message = "K được để trống password")
    String password;

    @NotEmpty(message = "K được để trống confirm password")
    String confirmPassword;
}
