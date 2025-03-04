$('#form-order').validate({
    rules: {
        email: {
            required: true,
            email: true
        },
        nameCustomer: {
            required: true
        },
        phone: {
            required: true,
            digits: true
        },
        province: {
            required: true
        },
        district: {
            required: true
        },
        ward: {
            required: true
        },
        addressDetail:{
            required: true
        },
        payment: {
            required: true
        },
        delivery: {
            required: true
        }
    },
    messages: {
        email: {
            required: "Vui lòng nhập email",
            email: "Email không hợp lệ"
        },
        nameCustomer: {
            required: "Vui lòng nhập tên người nhận"
        },
        phone: {
            required: "Vui lòng nhập số điện thoại",
            digits: "Số điện thoại chỉ chứa các chữ số"
        },
        province: {
            required: "Vui lòng chọn tỉnh/thành phố"
        },
        district: {
            required: "Vui lòng chọn quận/huyện"
        },
        ward: {
            required: "Vui lòng chọn phường/xã"
        },
        addressDetail:{
            required: "Vui lòng nhập địa chỉ cụ thể"
        },
        payment: {
            required: "Vui lòng chọn phương thức thanh toán"
        },
        delivery: {
            required: "Vui lòng chọn phương thức vận chuyển"
        }
    },
    errorElement: 'span',
    errorClass: 'invalid-feedback',
    errorPlacement: function(error, element) {
        error.addClass('invalid-feedback');
        element.closest('.form-group').append(error);
    },
    highlight: function(element, errorClass, validClass) {
        $(element).addClass('is-invalid');
    },
    unhighlight: function(element, errorClass, validClass) {
        $(element).removeClass('is-invalid');
    }
});

//Tạo ra CodeOrder cho đơn COD
const updateCodeOrder = async (orderId) => {
    try{
        let res =  await axios.put(`/api/orders/updateCodeOrder/${orderId}`)
        console.log(res)
        window.location.href = `/admin/orders/${res.data.codeOrder}`
    }catch (e){
        console.log(e)
        toastr.error(e.response.data.message);
    }

}

//Tạo orderDetail từ orderId
const createOrderDetail = async (orderId) => {
    // Sử dụng hàm callback trong each
    $('#orderDetail-group li').each(async function() {
        // Tạo đối tượng data cho từng phần tử
        const data = {
            orderId: orderId,
            productId: $(this).data('product-id'),
            colorId: $(this).data('color-id'),
            sizeId: $(this).data('size-id'),
            quantity: parseInt($(this).data('quantity'), 10)
        };

        console.log(data);

        try {
            // Gửi yêu cầu PUT với axios
            let res = await axios.put("/api/orderDetails", data);
            console.log('Response:', res.data);
        } catch (e) {
            console.log("Lỗi khi tạo orderDetail", e);
        }
    });
};
//Tạo ra link thanh toán từ orderId
const createPaymentResponse = async (orderId)=>{
    try {
        let res = await axios.get(`/api/payments/create_payment/${orderId}`)
        window.location.href = `/admin/orders/${res.data.codeOrder}`
    }catch (e){
        console.log(e)
        toastr.error(e.response.data.message);
    }
}

// Xử lý sự kiện nhấn nút Tạo
$('#btnCreateOrder').on('click', async function (e) {
    e.preventDefault();

    // Kiểm tra tính hợp lệ của biểu mẫu
    if (!$('#form-order').valid()) {
        return;
    }
    if (selectedUserId==""){
        toastr.error("Chưa chọn user")
        return;
    }

    // Lấy giá trị của "Giảm giá"
    const discountAmountText = document.getElementById('discount-amount').textContent.trim();
    // Chuyển đổi thành số
    const discountAmount = parseFloat(discountAmountText.replace(/[^0-9.-]/g, ''));

    // Lấy giá trị của "Tổng tiền"
    const finalTotalText = document.getElementById('final-total').textContent.trim();
    // Chuyển đổi thành số
    const finalTotal = parseFloat(finalTotalText.replace(/[^0-9.-]/g, ''));
    // Lấy giá trị của "Thành tiền"
    const totalPrice = parseFloat(document.getElementById('total-price').textContent.replace(/[^0-9]/g, '')) || 0;
    if (totalPrice <= 0) {
        toastr.error("Chưa có sản phẩm nào")
        return;
    }

    const email = $('#email').val();
    const nameCustomer = $('#nameCustomer').val();
    const phone = $('#phone').val();
    const province = $('#province').val();
    const district = $('#district').val();
    const ward = $('#ward').val();
    const payment = $('#payment').val();
    const addressDetail = $('#addressDetail').val();
    const delivery = $('#delivery').val();
    const notes = $('#orderNotes').val()

    let couponCode;
    let coupon = coupons.find(c => c.id === Number(selectedCouponId));
    if (coupon){
        couponCode=coupon.code;
    }else {
        couponCode="";
    }

    const data = {
        userId: selectedUserId,
        couponCode:  couponCode,
        receiverName: nameCustomer,
        email: email,
        phone: phone,
        province: province,
        district: district,
        ward: ward,
        addressDetail: addressDetail,
        notes: notes,
        payment: payment,
        delivery: delivery,
        totalPrice: totalPrice,
        discountAmount: discountAmount,
        finalTotal: finalTotal
    }

    try {
        let res = await axios.put("/api/orders",data)
        createOrderDetail(res.data.id)
        if (res.data.payment=="VNPAY"){
            createPaymentResponse(res.data.id)
        }else {
            updateCodeOrder(res.data.id)
        }
        toastr.success("Tạo đơn hàng thành công")
    }catch (e){
        toastr.error(e.response.data.message)
    }
});