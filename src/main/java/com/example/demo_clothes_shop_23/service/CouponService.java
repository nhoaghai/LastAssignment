package com.example.demo_clothes_shop_23.service;

import com.example.demo_clothes_shop_23.entities.Coupon;
import com.example.demo_clothes_shop_23.exception.ResourceNotFoundException;
import com.example.demo_clothes_shop_23.repository.CouponRepository;
import com.example.demo_clothes_shop_23.request.UpsertCouponRequest;
import com.example.demo_clothes_shop_23.request.UseCouponRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CouponService {
    private CouponRepository couponRepository;

    public List<Coupon> getByActiveTrue() {
        return couponRepository.findByActive(true);
    }

    public Coupon getCoupon(Integer userId,String couponCode) {
        // Tìm coupon theo mã và trạng thái hoạt động
        Coupon coupon = couponRepository.findByCodeAndActive(couponCode, true);

        // Kiểm tra xem coupon có tồn tại không
        if (coupon == null) {
            throw new ResourceNotFoundException("Không tìm thấy coupon này");
        }

        // Kiểm tra xem coupon có còn số lượng không
        if (coupon.getQuantity() == 0) {
            throw new RuntimeException("Coupon đã dùng hết");
        }

        LocalDateTime now = LocalDateTime.now();
        if (coupon.getStartDate() != null && now.isBefore(coupon.getStartDate())) {
            throw new RuntimeException("Coupon chưa đến thời gian sử dụng");
        }
        if (coupon.getEndDate() != null && now.isAfter(coupon.getEndDate())) {
            throw new RuntimeException("Coupon đã hết hạn sử dụng");
        }

        // Nếu danh sách chưa được khởi tạo, khởi tạo nó
        if (coupon.getListUserIdUsed() == null) {
            coupon.setListUserIdUsed(new ArrayList<>());
        }

        // Kiểm tra xem người dùng đã sử dụng coupon chưa
        if (coupon.getListUserIdUsed() != null && coupon.getListUserIdUsed().contains(userId)) {
            throw new RuntimeException("Người dùng đã sử dụng coupon này");
        }

        return coupon;
    }

    public List<Coupon> getAll() {
        return couponRepository.findAll();
    }

    public Coupon createCoupon(UpsertCouponRequest upsertCouponRequest) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate startDate;
        LocalDate endDate;
        try {
            startDate = LocalDate.parse(upsertCouponRequest.getStartDate(), formatter);
            endDate = LocalDate.parse(upsertCouponRequest.getEndDate(), formatter);

            LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.of(0, 0)); // 00:00:00
            LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));
            Coupon coupon = Coupon.builder()
                .code(upsertCouponRequest.getCode())
                .amount(upsertCouponRequest.getAmount())
                .active(upsertCouponRequest.getActive())
                .quantity(upsertCouponRequest.getQuantity())
                .listUserIdUsed(new ArrayList<>())
                .startDate(startDateTime)
                .endDate(endDateTime)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
            couponRepository.save(coupon);
            return coupon;
        }catch (DateTimeParseException e){
            throw new IllegalArgumentException("Ngày tháng không hợp lệ");
        }
    }

    public Coupon updateCoupon(UpsertCouponRequest upsertCouponRequest, Integer couponId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate startDate;
        LocalDate endDate;
        try {
            startDate = LocalDate.parse(upsertCouponRequest.getStartDate(), formatter);
            endDate = LocalDate.parse(upsertCouponRequest.getEndDate(), formatter);

            Coupon coupon = couponRepository.findById(couponId).orElseThrow(
                () -> new ResourceNotFoundException("Không tìm thấy coupon")
            );

            LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.of(0, 0)); // 00:00:00
            LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));

            coupon.setCode(upsertCouponRequest.getCode());
            coupon.setAmount(upsertCouponRequest.getAmount());
            coupon.setActive(upsertCouponRequest.getActive());
            coupon.setQuantity(upsertCouponRequest.getQuantity());
            coupon.setStartDate(startDateTime);
            coupon.setEndDate(endDateTime);
            coupon.setUpdatedAt(LocalDateTime.now());
            couponRepository.save(coupon);
            return coupon;
        }catch (DateTimeParseException e){
            throw new IllegalArgumentException("Ngày tháng không hợp lệ");
        }
    }

    public void deleteCoupon(Integer couponId) {
        couponRepository.deleteById(couponId);
    }
}
