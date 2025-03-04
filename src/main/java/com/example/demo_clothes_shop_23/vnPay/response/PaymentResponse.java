package com.example.demo_clothes_shop_23.vnPay.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class PaymentResponse implements Serializable {
    private String status;
    private String message;
    private String codeOrder;
    private String URL;
}
