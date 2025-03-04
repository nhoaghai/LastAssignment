package com.example.demo_clothes_shop_23.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateSubImageRequest {
    @NotNull(message = "K duoc null")
    MultipartFile formData;
    @NotNull(message = "K duoc null")
    Integer productId;
    @NotNull(message = "K duoc null")
    Integer colorId;

}
