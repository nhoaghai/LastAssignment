//Xử lý phần validate
$(document).ready(function() {
    $('#form-checkout').validate({
        rules: {
            nameCustomer: {
                required: true,
            },
            email: {
                required: true,
                email: true,
            },
            phone: {
                required: true,
            },
            province: {
                required: true,
            },
            district: {
                required: true,
            },
            ward: {
                required: true,
            },
            addressDetail: {
                required: true,
            },
            'payment-method': {
                required: true,
            },
            'delivery-method': {
                required: true,
            }
        },
        messages: {
            nameCustomer: {
                required: "Tên khách hàng không được để trống",
            },
            email: {
                required: "Email không được để trống",
                email: "Email không đúng định dạng",
            },
            phone: {
                required: "Số điện thoại không được để trống",
            },
            province: {
                required: "Không được để trống",
            },
            district: {
                required: "Không được để trống",
            },
            ward: {
                required: "Không được để trống",
            },
            addressDetail: {
                required: "Không được để trống",
            },
            'payment-method': {
                required: "Vui lòng chọn phương thức thanh toán",
            },
            'delivery-method': {
                required: "Vui lòng chọn phương thức vận chuyển",
            }
        },
        errorElement: 'span',
        errorPlacement: function (error, element) {
            error.addClass('invalid-feedback');
            if (element.is(':radio')) {
                if (element.attr('name') === 'payment-method') {
                    // Thêm lỗi vào dưới thẻ <div id="chose-payment">
                    $('#chose-payment').append(error);
                } else if (element.attr('name') === 'delivery-method') {
                    // Thêm lỗi vào dưới thẻ <div id="chose-deli">
                    $('#chose-deli').append(error);
                }
            } else {
                element.closest('.checkout__input').append(error);
            }
        },
        highlight: function (element, errorClass, validClass) {
            $(element).addClass('is-invalid');
        },
        unhighlight: function (element, errorClass, validClass) {
            $(element).removeClass('is-invalid');
        }
    });

    //Tạo ra CodeOrder cho đơn COD
    const updateCodeOrder = async (orderId) => {
        try{
            let res =  await axios.put(`/api/orders/updateCodeOrder/${orderId}`)
            console.log(res)
            window.location.href = `/cod-Return?codeOrder=${res.data.codeOrder}`
        }catch (e){
            console.log(e)
            toastr.error(e.response.data.message);
        }

    }

    //Tạo orderDetail từ orderId
    const createOrderDetail = async (orderId) => {
        for (const cart of cartsByUserId) {
            const data={
                orderId: orderId,
                productId: cart.product.id,
                colorId: cart.color.id,
                sizeId:  cart.size.id,
                quantity: cart.quantity
            }
            console.log(data)
            try {
                let res = await axios.put("/api/orderDetails",data)
            }catch (e){
                console.log("Lỗi khi tạo orderDetail")
            }
        }
    }
    //Tạo ra link thanh toán từ orderId
    const createPaymentResponse = async (orderId)=>{
        try {
            let res = await axios.get(`/api/payments/create_payment/${orderId}`)
            window.location.href = res.data.url;
        }catch (e){
            console.log(e)
            toastr.error(e.response.data.message);
        }
    }

    $('#form-checkout').on('submit', async function (e) {
        e.preventDefault();

        if (!$('#form-checkout').valid()) {
            return;
        }
        if (parseLong($('#total-price').text())==0){
            toastr.error("Bạn chưa mua sản phẩm nào")
            return;
        }

        const dataOrder = {
            userId: userId,
            couponCode: couponCode,
            receiverName: $('#input-nameCustomer').val(),
            email: $('#input-email').val(),
            phone: $('#input-phone').val(),
            province: $('#input-province').val(),
            district: $('#input-district').val(),
            ward: $('#input-ward').val(),
            addressDetail: $('#input-addressDetail').val(),
            notes: $('#input-orderNotes').val(),
            payment: $('input[name="payment-method"]:checked').val(),
            delivery: $('input[name="delivery-method"]:checked').val(),
            totalPrice: parseLong($('#total-price').text()),
            discountAmount: parseLong($('#discount-amount').text()),
            finalTotal: parseLong($('#final-total').text())
        };
        console.log(dataOrder)
        function parseLong(value) {
            return parseInt(value.replace(/[^0-9]/g, ''), 10) || 0;  // Loại bỏ ký tự không phải số và chuyển đổi thành số nguyên
        }

        try {
            let res = await axios.put("/api/orders", dataOrder);
            console.log(res)
            createOrderDetail(res.data.id)
            if (res.data.payment=="VNPAY"){
                createPaymentResponse(res.data.id)
            }else {
                updateCodeOrder(res.data.id)
            }
        } catch (e) {
            console.log(e)
            toastr.error(e.response.data.message);
        }
    });
});