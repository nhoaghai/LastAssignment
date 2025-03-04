package com.example.demo_clothes_shop_23.vnPay.controller;

import com.example.demo_clothes_shop_23.entities.Orders;
import com.example.demo_clothes_shop_23.model.enums.OrdersStatus;
import com.example.demo_clothes_shop_23.repository.OrdersRepository;
import com.example.demo_clothes_shop_23.service.OrderDetailService;
import com.example.demo_clothes_shop_23.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PaymentController {
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final OrdersRepository ordersRepository;

    @GetMapping("/payment_return")
    public String handlePaymentReturn(
        @RequestParam("vnp_Amount") String amount,
        @RequestParam("vnp_BankCode") String bankCode,
        @RequestParam(value = "vnp_BankTranNo",required = false) String bankTranNo,
        @RequestParam("vnp_CardType") String cardType,
        @RequestParam("vnp_OrderInfo") String orderInfo,
        @RequestParam("vnp_PayDate") String payDate,
        @RequestParam("vnp_ResponseCode") String responseCode,
        @RequestParam("vnp_TmnCode") String tmnCode,
        @RequestParam("vnp_TransactionNo") String transactionNo,
        @RequestParam("vnp_TransactionStatus") String transactionStatus,
        @RequestParam("vnp_TxnRef") String txnRef,
        @RequestParam("vnp_SecureHash") String secureHash,
        Model model) {


        // Thêm các tham số vào mô hình để gửi đến trang HTML
        model.addAttribute("vnp_Amount", amount);
        model.addAttribute("vnp_BankCode", bankCode);
        model.addAttribute("vnp_BankTranNo", bankTranNo);
        model.addAttribute("vnp_CardType", cardType);
        model.addAttribute("vnp_OrderInfo", orderInfo);
        model.addAttribute("vnp_PayDate", payDate);
        model.addAttribute("vnp_ResponseCode", responseCode);
        model.addAttribute("vnp_TmnCode", tmnCode);
        model.addAttribute("vnp_TransactionNo", transactionNo);
        model.addAttribute("vnp_TransactionStatus", transactionStatus); //lấy thông tin đơn hàng thanh toán thành công hay chưa
        model.addAttribute("vnp_TxnRef", txnRef);//
        model.addAttribute("vnp_SecureHash", secureHash);

        Orders order = orderService.getByCodeOrder(txnRef);
        if (transactionStatus!=null) {
            if (transactionStatus.equals("00")) {
                order.setStatus(OrdersStatus.CHO_GIAO_HANG);
                ordersRepository.save(order);
            }
        }
        model.addAttribute("order",order);
        model.addAttribute("orderDetails", orderDetailService.getByOrderId(order.getId()));

        return "web/vnPay-Return";
    }
}
