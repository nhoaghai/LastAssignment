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
public class UpsertCouponRequest {
    @NotEmpty(message = "K được để trống")
    String code;
    @NotEmpty(message = "K được để trống")
    Integer amount;
    @NotEmpty(message = "K được để trống")
    Integer quantity;
    @NotEmpty(message = "K được để trống")
    Boolean active;
    @NotEmpty(message = "K được để trống")
    String startDate;
    @NotEmpty(message = "K được để trống")
    String endDate;
}
