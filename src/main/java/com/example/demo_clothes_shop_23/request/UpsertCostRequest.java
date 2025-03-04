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
public class UpsertCostRequest {
    @NotEmpty(message = "Không được để nội dung trống")
    String name;
    @NotEmpty(message = "Không được để nội dung trống")
    Long amount;
    @NotEmpty(message = "Không được để nội dung trống")
    String description;
    @NotEmpty(message = "Không được để nội dung trống")
    Integer user;
}
