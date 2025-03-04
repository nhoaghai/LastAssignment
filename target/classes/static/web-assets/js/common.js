toastr.options = {
    "closeButton": false,
    "debug": false,
    "newestOnTop": false,
    "progressBar": false,
    "positionClass": "toast-top-right",
    "preventDuplicates": false,
    "onclick": null,
    "showDuration": "300",
    "hideDuration": "1000",
    "timeOut": "5000",
    "extendedTimeOut": "1000",
    "showEasing": "swing",
    "hideEasing": "linear",
    "showMethod": "fadeIn",
    "hideMethod": "fadeOut"
}
function formatCurrency(value) {
    if (typeof value !== 'number' || isNaN(value)) {
        console.error("Giá trị không phải là số:", value);
        return '0 VNĐ'; // Giá trị mặc định nếu không phải số
    }
    return value.toLocaleString('vi-VN') + ' VNĐ'; // Định dạng số nguyên với định dạng tiền tệ VNĐ
}

// Hàm chuyển đổi chuỗi thành Long
function parseLong(value) {
    return parseInt(value.replace(/[^0-9]/g, ''), 10) || 0;  // Loại bỏ ký tự không phải số và chuyển đổi thành số nguyên
}