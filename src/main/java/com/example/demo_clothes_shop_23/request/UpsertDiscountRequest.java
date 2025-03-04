package com.example.demo_clothes_shop_23.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpsertDiscountRequest {
    @NotEmpty(message = "K được để trống")
    String name;
    @NotEmpty(message = "K được để trống")
    @Column(columnDefinition = "TEXT")
    String description;
    @NotEmpty(message = "K được để trống")
    String type;
    @NotEmpty(message = "K được để trống")
    Long amount;
    @NotEmpty(message = "K được để trống")
    Boolean active;
    @NotEmpty(message = "K được để trống")
    String startDate;
    @NotEmpty(message = "K được để trống")
    String endDate;
    List<Integer> productIds;
}
