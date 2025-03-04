package com.example.demo_clothes_shop_23.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpsertProductRequest {
    @NotEmpty(message = "K được để trống")
    String name;
    @NotEmpty(message = "K được để trống")
    String description;
    @NotEmpty(message = "K được để trống")
    Long price;
    @NotEmpty(message = "K được để trống")
    Integer categoryId;
    @NotEmpty(message = "K được để trống")
    List<Integer> colorIds;
    @NotEmpty(message = "K được để trống")
    List<Integer> sizeIds;
    @NotEmpty(message = "K được để trống")
    Boolean status;

    Map<String,Integer> quantityData;
}
