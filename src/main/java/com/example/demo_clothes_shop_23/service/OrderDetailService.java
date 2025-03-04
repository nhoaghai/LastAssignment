package com.example.demo_clothes_shop_23.service;

import com.example.demo_clothes_shop_23.entities.*;
import com.example.demo_clothes_shop_23.repository.*;
import com.example.demo_clothes_shop_23.request.CreateOrderDetailRequest;
import com.example.demo_clothes_shop_23.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderDetailService {
    private final OrdersRepository ordersRepository;
    private final ProductRepository productRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;
    private final OrdersDetailRepository ordersDetailRepository;
    private final CartRepository cartRepository;
    private final QuantityService quantityService;
    private final QuantityRepository quantityRepository;

    public List<OrdersDetail> getByOrderId(Integer orderId) {
        return ordersDetailRepository.findByOrdersId(orderId);
    }

    public OrdersDetail createOrderDetail(CreateOrderDetailRequest createOrderDetailRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = (User) userDetails.getUser();
        Orders order = ordersRepository.findById(createOrderDetailRequest.getOrderId())
            .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng này"));
        Product product = productRepository.findById(createOrderDetailRequest.getProductId())
            .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm này"));
        Color color = colorRepository.findById(createOrderDetailRequest.getColorId())
            .orElseThrow(() -> new RuntimeException("Không tìm thấy màu này"));
        Size size = sizeRepository.findById(createOrderDetailRequest.getSizeId())
            .orElseThrow(() -> new RuntimeException("Không tìm thấy size này"));
        List<Cart> carts = cartRepository.findByUser_IdOrderByCreatedAt(user.getId());
        cartRepository.deleteAll(carts);

        Quantity quantity = quantityService.getByProductIdAndColorIdAndSizeId(product.getId(), color.getId(), size.getId());
        quantity.setValue(quantity.getValue()-createOrderDetailRequest.getQuantity());
        quantity.setUpdatedAt(LocalDateTime.now());
        quantityRepository.save(quantity);

        OrdersDetail ordersDetail = OrdersDetail.builder()
            .orders(order)
            .product(product)
            .color(color)
            .size(size)
            .quantity(createOrderDetailRequest.getQuantity())
            .price(product.getNewPrice()*createOrderDetailRequest.getQuantity())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        ordersDetailRepository.save(ordersDetail);
        return ordersDetail;
    }
}
