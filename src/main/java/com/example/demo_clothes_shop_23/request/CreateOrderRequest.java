package com.example.demo_clothes_shop_23.request;

import com.example.demo_clothes_shop_23.model.enums.DeliveryType;
import com.example.demo_clothes_shop_23.model.enums.PaymentType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateOrderRequest {
    Integer userId;

    String couponCode;

    @NotEmpty(message = "K được để trống")
    String receiverName;

    @NotEmpty(message = "Email k được để trống")
    @Email(message = "Email k đúng định dạng")
    String email;

    @NotEmpty(message = "K được để trống")
    String phone;

    @NotEmpty(message = "K được để trống")
    String province;

    @NotEmpty(message = "K được để trống")
    String district;

    @NotEmpty(message = "K được để trống")
    String ward;

    @NotEmpty(message = "K được để trống")
    String addressDetail;

    String notes;

    @NotEmpty(message = "K được để trống")
    String payment;

    @NotEmpty(message = "K được để trống")
    String delivery;

    @Column(nullable = false)
    Long totalPrice;

    Long discountAmount;

    @Column(nullable = false)
    Long finalTotal;
}
