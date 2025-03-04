package com.example.demo_clothes_shop_23.model.enums;

public enum SizeType {
    CLOTHES_SIZE,SHOES_SIZE;

    public String getName() {
        return switch (this) {
            case CLOTHES_SIZE -> "Khối lượng tịnh";
            case SHOES_SIZE -> "Kích thước giày";
            default -> throw new IllegalArgumentException("Unknown SizeType: " + this);
        };
    }
}
