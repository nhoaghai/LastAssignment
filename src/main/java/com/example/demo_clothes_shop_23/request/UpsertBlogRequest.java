package com.example.demo_clothes_shop_23.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpsertBlogRequest {
    @NotBlank(message = "K đc để trống title")
    String title;
    @NotBlank(message = "K đc để trống content")
    String content;
    @NotBlank(message = "K đc để trống description")
    String description;

    List<Integer> tagIds;

    Boolean status;
}
