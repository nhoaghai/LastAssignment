//COUPON
let selectedCouponId=""

//Khi nào có đơn hàng trhì nút chọn mã giảm giá hiện
function showBtnCouponModal() {
    const totalPrice = parseFloat(document.getElementById('total-price').textContent.replace(/[^0-9]/g, '')) || 0;
    const btnModalCoupon = document.getElementById('btnModalCoupon');
    const btnRemoveCoupon = document.getElementById('btnRemoveCoupon');

    // Nếu tổng tiền > 0, hiện nút chọn mã giảm giá
    btnModalCoupon.classList.toggle('d-none', totalPrice <= 0);
    console.log(totalPrice)
    if (totalPrice <= 0){
        selectedCouponId="";
        document.getElementById('coupon-select').value=selectedCouponId
    }

    // Nếu có mã giảm giá đã được chọn, hiện nút hủy mã giảm giá
    btnRemoveCoupon.classList.toggle('d-none', selectedCouponId == "");
}

    showBtnCouponModal()


// Mở modal cho mã giảm giá

document.getElementById('btnModalCoupon').addEventListener('click', function () {
    if (selectedCouponId==""){
        document.getElementById("btnSelectCoupon").disabled=true;
    }else {
        document.getElementById('coupon-select').value=selectedCouponId
    }
    $('#modalCoupon').modal('show');

});

document.getElementById("coupon-select").addEventListener("change",()=>{
    if (document.getElementById('coupon-select').value==""){
        document.getElementById("btnSelectCoupon").disabled=true;
    }else {
        document.getElementById("btnSelectCoupon").disabled=false;
        console.log('Mã giảm giá đã chọn:', selectedCouponId);
    }
})

document.getElementById("btnSelectCoupon").addEventListener("click", () => {
    // Hiển thị nút Hủy mã giảm giá
    document.getElementById("btnRemoveCoupon").classList.remove("d-none");
    selectedCouponId=document.getElementById('coupon-select').value
    const coupon = coupons.find(c => c.id === Number(selectedCouponId));
    console.log(coupon)
    if (coupon) {
        applyCoupon(coupon);
    }
    $('#modalCoupon').modal('hide');
});

document.getElementById("btnRemoveCoupon").addEventListener("click", () => {
    // Ẩn nút Hủy mã giảm giá
    document.getElementById("btnRemoveCoupon").classList.add("d-none");
    selectedCouponId=""
    document.getElementById('coupon-select').value=selectedCouponId
    removeCoupon();
});

function applyCoupon(coupon) {
    const totalPrice = parseFloat(document.getElementById('total-price').textContent.replace(/[^0-9]/g, '')) || 0;
    const discount = (totalPrice * coupon.amount) / 100;
    const finalTotal = totalPrice - discount;

    // Cập nhật thông tin giảm giá và tổng tiền
    document.getElementById('discount-amount').textContent = formatCurrency(discount);
    document.getElementById('final-total').textContent = formatCurrency(finalTotal);
}

function removeCoupon() {
    // Khôi phục tổng tiền về giá trị trước khi giảm giá
    const originalTotal = parseFloat(document.getElementById('total-price').textContent.replace(/[^0-9]/g, '')) || 0;
    const discountAmount = parseFloat(document.getElementById('discount-amount').textContent.replace(/[^0-9]/g, '')) || 0;
    const finalTotal = originalTotal;

    // Cập nhật thông tin giảm giá và tổng tiền
    document.getElementById('discount-amount').textContent = '0 VNĐ';
    document.getElementById('final-total').textContent = formatCurrency(finalTotal);
}

function formatCurrency(amount) {
    return `${amount.toLocaleString('vi-VN')} VNĐ`;
}
