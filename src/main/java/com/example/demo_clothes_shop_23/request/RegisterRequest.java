package com.example.demo_clothes_shop_23.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {
    @NotEmpty(message = "K được để trống name")
    String name;

    @NotEmpty(message = "Không được để trống email")
    @Email(message = "Email không đúng định dạng")
    String email;

    @Column(nullable = false)
    String receiverName;

    @Column(nullable = false)
    String phone;

    @Column(nullable = false)
    String province;

    @Column(nullable = false)
    String district;

    @Column(nullable = false)
    String ward;

    @Column(columnDefinition = "TEXT")
    String addressDetail;

    @NotEmpty(message = "K được để trống password")
    String password;

    @NotEmpty(message = "K được để trống confirm password")
    String confirmPassword;


}

