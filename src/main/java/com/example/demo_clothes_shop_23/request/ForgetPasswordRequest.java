package com.example.demo_clothes_shop_23.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ForgetPasswordRequest {
    @NotEmpty(message = "Email k được để trống")
    @Email(message = "Email k đúng định dạng")
    String email;
}
