package com.example.demo_clothes_shop_23.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdatePasswordRequest {
    @NotEmpty(message = "Không được để trống mật khẩu cũ")
    String oldPassword;
    @NotEmpty(message = "Không được để trống mật khẩu mới")
    String newPassword;
    @NotEmpty(message = "Không được để trống mật khẩu mới confirm")
    String confirmPassword;
}
