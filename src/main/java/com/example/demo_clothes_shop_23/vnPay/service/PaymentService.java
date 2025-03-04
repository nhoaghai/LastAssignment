package com.example.demo_clothes_shop_23.vnPay.service;

import com.example.demo_clothes_shop_23.entities.Orders;
import com.example.demo_clothes_shop_23.repository.OrdersRepository;
import com.example.demo_clothes_shop_23.vnPay.config.PaymentConfig;
import com.example.demo_clothes_shop_23.vnPay.response.PaymentResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AllArgsConstructor
public class PaymentService {
    private final OrdersRepository ordersRepository;

    public PaymentResponse createPaymentResponse(Integer orderId, HttpServletRequest request) {
        Orders order = ordersRepository.findById(orderId).orElseThrow(
            () -> new RuntimeException("Order not found")
        );

        long amountInCents = order.getFinalTotal()*100; // Ví dụ số tiền: 10,000 VND = 1000000 cents
        String vnp_TxnRef = PaymentConfig.getRandomNumber(8);
        String vnp_IpAddr = PaymentConfig.getIpAddress(request);

        String vnp_TmnCode = PaymentConfig.vnp_TmnCode;
        order.setCodeOrder(vnp_TxnRef);
        ordersRepository.save(order);
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", PaymentConfig.vnp_Version);
        vnp_Params.put("vnp_Command", PaymentConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amountInCents));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", PaymentConfig.orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", PaymentConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        //Billing
        vnp_Params.put("vnp_Bill_Mobile", order.getPhone());
        vnp_Params.put("vnp_Bill_Email", order.getEmail());
        String fullName = (order.getReceiverName()).trim();
        if (!fullName.isEmpty()) {
            int idx = fullName.indexOf(' ');
            String firstName = fullName.substring(0, idx);
            String lastName = fullName.substring(fullName.lastIndexOf(' ') + 1);
            vnp_Params.put("vnp_Bill_FirstName", firstName);
            vnp_Params.put("vnp_Bill_LastName", lastName);

        }
        vnp_Params.put("vnp_Bill_Address", order.getAddressDetail());

        // Sắp xếp và mã hóa các tham số
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                // Xây dựng dữ liệu hash
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                // Xây dựng truy vấn
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = PaymentConfig.hmacSHA512(PaymentConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = PaymentConfig.vnp_PayUrl + "?" + queryUrl;

        return PaymentResponse.builder()
            .status("OK")
            .message("Successfully created payment link: " + vnp_TxnRef)
            .codeOrder(vnp_TxnRef)
            .URL(paymentUrl)
            .build();
    }
}
