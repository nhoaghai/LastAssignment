//PRODUCT
// Mở modal cho sản phẩm
let selectedProductId

document.getElementById('btnModalProduct').addEventListener('click', function () {
    $('#modalProduct').modal('show');
    selectedProductId = '';
    $('#product').val(selectedProductId).trigger('change');
});

$('#product').on('change', function () {
    selectedProductId = $(this).val();
    if (selectedProductId !== '') {
        console.log(selectedProductId);

        const product = products.find(p => p.id == selectedProductId);
        console.log(product)
        if (product) {
            let colors = product.colors;
            let sizes = product.sizes;
            // Sắp xếp màu sắc theo id

            colors.sort((a, b) => a.id - b.id);
            sizes.sort((a, b) => a.orders - b.orders);

            console.log(colors);
            console.log(sizes);

            // Cập nhật UI với màu sắc và kích thước
            updateProductDetails(colors, sizes);
        }
    } else {
        // Nếu không có sản phẩm được chọn, làm trống và vô hiệu hóa các trường màu sắc và kích thước
        updateProductDetails([], []);
    }
});

function updateProductDetails(colors, sizes) {
    const $colorSelect = $('#color');
    const $sizeSelect = $('#size');

    // Cập nhật danh sách màu sắc
    $colorSelect.empty(); // Xóa tất cả các tùy chọn hiện có
    $colorSelect.append('<option value="">Chọn màu sắc</option>'); // Thêm tùy chọn mặc định
    colors.forEach(color => {
        $colorSelect.append(`<option value="${color.id}">${color.colorName}</option>`);
    });
    $colorSelect.prop('disabled', colors.length === 0); // Vô hiệu hóa nếu không có màu sắc

    // Cập nhật danh sách kích thước
    $sizeSelect.empty(); // Xóa tất cả các tùy chọn hiện có
    $sizeSelect.append('<option value="">Chọn kích thước</option>'); // Thêm tùy chọn mặc định
    sizes.forEach(size => {
        $sizeSelect.append(`<option value="${size.id}">${size.sizeName}</option>`);
    });
    $sizeSelect.prop('disabled', sizes.length === 0); // Vô hiệu hóa nếu không có kích thước
}

//Validate
$('#form-product').validate({
    rules: {
        product: {
            required: true,
        },
        color: {
            required: true,
        },
        size: {
            required: true,
        },
        quantity: {
            required: true,
            min:1
        },
    },
    messages: {
        product: {
            required: "Vui lòng chọn sản phẩm",
        },
        color: {
            required: "Vui lòng chọn màu sắc",
        },
        size: {
            required: "Vui lòng chọn kích thước",
        },
        quantity: {
            required: "Vui lòng chọn số lượng",
            min: "Vui lòng chọn số lớn hơn 0"
        },
    },
    errorElement: 'span',
    errorPlacement: function (error, element) {
        error.addClass('invalid-feedback');
        element.closest('.form-group').append(error);
    },
    highlight: function (element, errorClass, validClass) {
        $(element).addClass('is-invalid');
    },
    unhighlight: function (element, errorClass, validClass) {
        $(element).removeClass('is-invalid');
    }
});

function formatCurrency(value) {
    return value.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
}


async function checkQuantity(productId, colorId, sizeId, quantity) {
    try {
        // Gửi yêu cầu kiểm tra số lượng tồn kho từ API
        let res = await axios.get(`/api/admin/quantities/product/${productId}/color/${colorId}/size/${sizeId}`);
        // Kiểm tra số lượng tồn kho có đủ cho yêu cầu
        return res.data.value >= quantity;
    } catch (e) {
        // Hiển thị lỗi nếu có lỗi xảy ra khi gọi API
        toastr.error(e.response?.data?.message || 'Có lỗi xảy ra khi kiểm tra số lượng');
        return false; // Trả về false nếu có lỗi
    }
}

// Xử lý sự kiện submit của form sản phẩm
$('#form-product').on('submit', async (e) => {
    e.preventDefault();
    console.log("Form submission attempted");

    // Kiểm tra tính hợp lệ của biểu mẫu
    if (!$('#form-product').valid()) {
        return;
    }

    // Lấy thông tin từ biểu mẫu
    const product = products.find(p => p.id == selectedProductId);
    const productName = $('#product').find('option:selected').text();
    const colorId = $('#color').find('option:selected').val();
    const colorName = $('#color').find('option:selected').text();
    const sizeId = $('#size').find('option:selected').val();
    const sizeName = $('#size').find('option:selected').text();
    const quantity = parseInt($('#quantity').val(), 10);

    // Kiểm tra số lượng tồn kho trước khi thực hiện các hành động khác
    if (!await checkQuantity(selectedProductId, colorId, sizeId, quantity)) {
        toastr.error("Không đủ hàng trong kho");
        return;
    }

    // Giả sử giá sản phẩm là giá mới từ đối tượng sản phẩm
    const pricePerUnit = product.newPrice;
    const totalPrice = formatCurrency(pricePerUnit * quantity);

    // Tìm sản phẩm đã tồn tại trong danh sách đơn hàng
    let $orderDetailGroup = $('#orderDetail-group');
    let $existingProduct = $orderDetailGroup.find(`li[data-product-id="${selectedProductId}"][data-color-id="${colorId}"][data-size-id="${sizeId}"]`);

    if ($existingProduct.length > 0) {
        // Nếu sản phẩm đã tồn tại, cập nhật số lượng và giá
        let existingQuantity = parseInt($existingProduct.data('quantity'), 10);
        let newQuantity = existingQuantity + quantity;

        // Kiểm tra số lượng tồn kho với số lượng mới
        if (!await checkQuantity(selectedProductId, colorId, sizeId, newQuantity)) {
            toastr.error("Không đủ hàng trong kho");
            return;
        }

        // Cập nhật số lượng và giá
        $existingProduct.data('quantity', newQuantity);
        $existingProduct.find('.order-detail-price').text(formatCurrency(pricePerUnit * newQuantity));
        $existingProduct.html(`
            ${newQuantity} x ${productName}<br>
            &emsp;- Màu: ${colorName}<br>
            &emsp;- Kích thước: ${sizeName}
            <span>
            <span class="order-detail-price formatted-price">${formatCurrency(pricePerUnit * newQuantity)}</span>
             <button type="button" class="btn btn-danger btn-sm remove-item" data-product-id="${selectedProductId}" data-color-id="${colorId}" data-size-id="${sizeId}">X</button>
            </span>
          
        `);
    } else {
        // Nếu sản phẩm chưa tồn tại, thêm sản phẩm mới vào danh sách
        $orderDetailGroup.append(`
            <li data-product-id="${selectedProductId}" data-color-id="${colorId}" data-size-id="${sizeId}" data-quantity="${quantity}">
                ${quantity} x ${productName}<br>
                &emsp;- Màu: ${colorName}<br>
                &emsp;- Kích thước: ${sizeName}
                <span>
                <span class="order-detail-price formatted-price">${totalPrice}</span>
                <button type="button" class="btn btn-danger btn-sm remove-item" data-product-id="${selectedProductId}" data-color-id="${colorId}" data-size-id="${sizeId}">X</button>
                </span>
            </li>
        `);
    }

    // Cập nhật thông tin tổng tiền
    const currentTotal = parseFloat($('#total-price').text().replace(/[^0-9]/g, '')) || 0;
    const newTotal = currentTotal + (pricePerUnit * quantity);

    updateTotalPrice(newTotal)

    showBtnCouponModal()

    // Reset biểu mẫu
    document.getElementById("form-product").reset();

    // Ẩn modal sau khi gửi thông tin
    $('#modalProduct').modal('hide');
});

// Xử lý sự kiện click để xóa sản phẩm khỏi đơn hàng
$(document).on('click', '.remove-item', function() {
    // Lấy thông tin sản phẩm từ thuộc tính dữ liệu
    const productId = $(this).data('product-id');
    const colorId = $(this).data('color-id');
    const sizeId = $(this).data('size-id');

    // Xóa sản phẩm khỏi danh sách
    let $orderDetailGroup = $('#orderDetail-group');
    let $itemToRemove = $orderDetailGroup.find(`li[data-product-id="${productId}"][data-color-id="${colorId}"][data-size-id="${sizeId}"]`);
    let itemQuantity = parseInt($itemToRemove.data('quantity'), 10);
    let itemPrice = parseFloat($itemToRemove.find('.order-detail-price').text().replace(/[^0-9]/g, ''));

    // Cập nhật tổng tiền sau khi xóa
    const currentTotal = parseFloat($('#total-price').text().replace(/[^0-9]/g, '')) || 0;
    const newTotal = currentTotal - itemPrice;


    updateTotalPrice(newTotal)

    showBtnCouponModal()

    // Xóa sản phẩm khỏi danh sách
    $itemToRemove.remove();
});
function updateTotalPrice(currentTotal) {

    // Cập nhật tổng tiền và giảm giá
    let discount = 0;
    if (selectedCouponId) {
        const coupon = coupons.find(c => c.id === Number(selectedCouponId));
        if (coupon) {
            discount = (currentTotal * coupon.amount) / 100;
        }
    }

    const finalTotal = currentTotal - discount;

    // Cập nhật thông tin tổng tiền
    $('#total-price').text(formatCurrency(currentTotal));
    $('#discount-amount').text(formatCurrency(discount));
    $('#final-total').text(formatCurrency(finalTotal));
}
