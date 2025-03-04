package com.example.demo_clothes_shop_23.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpsertAddressRequest {
    Integer userId;
    @NotEmpty(message = "Không được để trống")
    String receiverName;
    @NotEmpty(message = "Không được để trống")
    String phone;
    @NotEmpty(message = "Không được để trống")
    String province;
    @NotEmpty(message = "Không được để trống")
    String district;
    @NotEmpty(message = "Không được để trống")
    String ward;
    @Column(columnDefinition = "TEXT")
    String addressDetail;
    Boolean chosen;
}
