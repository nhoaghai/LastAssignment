package com.example.demo_clothes_shop_23.service;

import com.example.demo_clothes_shop_23.entities.*;
import com.example.demo_clothes_shop_23.exception.ResourceNotFoundException;
import com.example.demo_clothes_shop_23.model.enums.DeliveryType;
import com.example.demo_clothes_shop_23.model.enums.OrdersStatus;
import com.example.demo_clothes_shop_23.model.enums.PaymentType;
import com.example.demo_clothes_shop_23.repository.*;
import com.example.demo_clothes_shop_23.request.CreateOrderRequest;
import com.example.demo_clothes_shop_23.security.CustomUserDetails;
import com.example.demo_clothes_shop_23.vnPay.config.PaymentConfig;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class OrderService {
    private final CouponRepository couponRepository;
    private final OrdersRepository ordersRepository;
    private final OrdersDetailRepository ordersDetailRepository;
    private final QuantityRepository quantityRepository;
    private final UserRepository userRepository;


    public Object getAll() {
        return ordersRepository.findAll();}

    public Orders getByCodeOrder(String codeOrder) {
        return ordersRepository.findByCodeOrder(codeOrder);
    }

    public Orders getById(Integer id) {
        return ordersRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Không tìm thấy đơn hàng này")
        );
    }

    public List<Orders> getByCurrentUser_IdOrderByCreatedAtDesc() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = (User) userDetails.getUser();
        return ordersRepository.findByUser_IdOrderByCreatedAtDesc(user.getId());
    }

    public List<Orders> getByUser_IdOrderByCreatedAtDesc(Integer userId) {
        return ordersRepository.findByUser_IdOrderByCreatedAtDesc(userId);
    }

    public List<Orders> getOrdersCreatedThisMonth() {
        LocalDateTime now = LocalDateTime.now();
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();
        return ordersRepository.findOrdersCreatedThisMonth(currentMonth, currentYear);
    }

    public Long getTotalFinalTotalCreatedThisMonth() {
        LocalDateTime now = LocalDateTime.now();
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();
        return ordersRepository.findTotalFinalTotalCreatedThisMonth(currentMonth, currentYear, OrdersStatus.DA_GIAO);
    }

    public Orders createOrder(CreateOrderRequest createOrderRequest) {
        User user = userRepository.findById(createOrderRequest.getUserId()).orElseThrow(
            () -> new ResourceNotFoundException("User not found")
        );

        if (createOrderRequest.getCouponCode() != null && !createOrderRequest.getCouponCode().isEmpty()) {
            Coupon coupon = couponRepository.findByCodeAndActive(createOrderRequest.getCouponCode(), true);

            if (coupon == null) {
                throw new RuntimeException("Mã giảm giá không hợp lệ");
            }

            // Kiểm tra số lượng mã giảm giá
            if (coupon.getQuantity() <= 0) {
                throw new RuntimeException("Mã giảm giá đã hết số lượng");
            }

            // Kiểm tra thời gian sử dụng
            LocalDateTime now = LocalDateTime.now();
            if (coupon.getStartDate() != null && now.isBefore(coupon.getStartDate())) {
                throw new RuntimeException("Mã giảm giá chưa đến thời gian sử dụng");
            }
            if (coupon.getEndDate() != null && now.isAfter(coupon.getEndDate())) {
                throw new RuntimeException("Mã giảm giá đã hết hạn sử dụng");
            }

            // Kiểm tra người dùng đã sử dụng mã giảm giá chưa
            List<Integer> usedUserIds = coupon.getListUserIdUsed();
            if (usedUserIds == null) {
                usedUserIds = new ArrayList<>();
            }
            if (usedUserIds.contains(createOrderRequest.getUserId())) {
                throw new RuntimeException("Người dùng đã sử dụng mã giảm giá này");
            }

            // Cập nhật mã giảm giá
            coupon.setQuantity(coupon.getQuantity() - 1);
            usedUserIds.add(createOrderRequest.getUserId());
            coupon.setListUserIdUsed(usedUserIds);
            couponRepository.save(coupon);
        }

        Orders orders = Orders.builder()
            .user(user)
            .email(createOrderRequest.getEmail())
            .receiverName(createOrderRequest.getReceiverName())
            .phone(createOrderRequest.getPhone())
            .province(createOrderRequest.getProvince())
            .district(createOrderRequest.getDistrict())
            .ward(createOrderRequest.getWard())
            .addressDetail(createOrderRequest.getAddressDetail())
            .totalPrice(createOrderRequest.getTotalPrice())
            .discountAmount(createOrderRequest.getDiscountAmount())
            .finalTotal(createOrderRequest.getFinalTotal())
            .status(OrdersStatus.CHO_XAC_NHAN)
            .payment(PaymentType.valueOf(createOrderRequest.getPayment()))
            .delivery(DeliveryType.valueOf(createOrderRequest.getDelivery()))
            .notes(createOrderRequest.getNotes())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        ordersRepository.save(orders);
        return orders;
    }

    public Orders updateCodeOrder(Integer orderId) {
        Orders order = ordersRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng này"));

        order.setCodeOrder(PaymentConfig.getRandomNumber(8));
        ordersRepository.save(order);
        return order;
    }

    public Orders cancelOrder(Integer orderId) {
        Orders order = ordersRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng này"));
        List<OrdersDetail> ordersDetails = ordersDetailRepository.findByOrdersId(orderId);
        ordersDetails.forEach(ordersDetail -> {
           Quantity quantity = quantityRepository.findByProductIdAndColorIdAndSizeId(
               ordersDetail.getProduct().getId(),
               ordersDetail.getColor().getId(),
               ordersDetail.getSize().getId()
           );
           quantity.setValue(quantity.getValue() + ordersDetail.getQuantity());
           quantityRepository.save(quantity);
        });
        order.setStatus(OrdersStatus.DA_HUY);
        ordersRepository.save(order);
        return order;
    }

    public void updateStatus(String status, Integer orderId) {
        Orders order = ordersRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng này"));
        OrdersStatus newStatus = OrdersStatus.valueOf(status);
        // Nếu trạng thái mới là DA_HUY, xử lý số lượng sản phẩm và cập nhật trạng thái
        if (newStatus == OrdersStatus.DA_HUY) {
            List<OrdersDetail> ordersDetails = ordersDetailRepository.findByOrdersId(orderId);
            ordersDetails.forEach(ordersDetail -> {
                Quantity quantity = quantityRepository.findByProductIdAndColorIdAndSizeId(
                    ordersDetail.getProduct().getId(),
                    ordersDetail.getColor().getId(),
                    ordersDetail.getSize().getId()
                );
                quantity.setValue(quantity.getValue() + ordersDetail.getQuantity());
                quantityRepository.save(quantity);
            });
        }

        // Cập nhật trạng thái đơn hàng
        order.setStatus(newStatus);
        ordersRepository.save(order);
    }
}
