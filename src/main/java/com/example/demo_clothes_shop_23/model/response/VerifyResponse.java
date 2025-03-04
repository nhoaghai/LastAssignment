package com.example.demo_clothes_shop_23.model.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerifyResponse {
     Boolean success;
     String message;
}
