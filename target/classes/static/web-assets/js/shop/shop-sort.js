/*Nice select*/
$("select").niceSelect();

/*Xử lý lọc category*/
function UpdateCategoryParent (categoryId){
    var url = new URL(window.location.href);
    var activeCate = document.querySelector('.shop__sidebar__categories label.black')
    console.log(activeCate)
    if (activeCate){
        var currentCategoryId = url.searchParams.get('categoryParentId');
        console.log(currentCategoryId)
        console.log(categoryId)
        console.log()
        if (currentCategoryId == categoryId) {
            url.searchParams.delete('categoryParentId');
            url.searchParams.delete('categoryChildId');
            activeCate.classList.remove('black');
            url.searchParams.set('page','1')
        } else {
            url.searchParams.delete('categoryChildId');
            url.searchParams.set('categoryParentId',categoryId)
            url.searchParams.set('page','1')
        }
        window.location.href = url.toString();
    }else {
        url.searchParams.set('categoryParentId',categoryId);
        url.searchParams.delete('categoryChildId');
        url.searchParams.set('page','1')
        window.location.href = url.toString();
    }
}

function UpdateCategoryChild (categoryId){
    var url = new URL(window.location.href);
    var activeCate = document.querySelector('.shop__sidebar__categories label.black')
    if (activeCate){
        var currentCategoryId = url.searchParams.get('categoryChildId');

        if (currentCategoryId == categoryId) {
            url.searchParams.delete('categoryChildId');
            url.searchParams.delete('categoryParentId');
            activeCate.classList.remove('black');
            url.searchParams.set('page','1')
        } else {
            url.searchParams.set('categoryChildId',categoryId)
            url.searchParams.delete('categoryParentId');
            url.searchParams.set('page','1')
        }
        window.location.href = url.toString();
    }else {
        url.searchParams.set('categoryChildId',categoryId)
        url.searchParams.delete('categoryParentId');
        url.searchParams.set('page','1')
        window.location.href = url.toString();
    }
}

/* Xử lý khi người dùng chọn size */
function updateUrlAndLabel() {
    /* Xử Lý size*/
    var url = new URL(window.location.href); // Lấy URL hiện tại
    var sizeId = url.searchParams.get('sizeId'); // Lấy sizeId từ URL

    if (sizeId) {
        var activeLabel = document.querySelector('#size-' + sizeId); // Tìm label có sizeId tương ứng

        if (activeLabel) {
            // Thêm class active vào label tìm được
            activeLabel.classList.add('active');

            // Bỏ class active khỏi các label còn lại
            var sizeLabels = document.querySelectorAll('.size-label');
            sizeLabels.forEach(function (label) {
                if (label !== activeLabel) {
                    label.classList.remove('active');
                }
            });
        }
    }

    /* Xử Lý màu*/
    var colorId = url.searchParams.get('colorId'); // Lấy colorId từ URL

    if (colorId) {
        var activeLabel2 = document.querySelector('#color-' + colorId); // Tìm label có for tương ứng với colorId

        if (activeLabel2) {
            activeLabel2.classList.add('selected'); // Thêm class selected vào label
        }
    }

    /*Xử lý ô search*/
    var nameKeyword = url.searchParams.get('nameKeyword'); // Lấy nameKeyword từ URL

    if (nameKeyword) {
        var searchInputEl = document.querySelector("#searchInput"); // Tìm label có for tương ứng với colorId
        searchInputEl.value=nameKeyword;
    }

    var startPrice = url.searchParams.get('startPrice')
    if (startPrice) {
        // Tìm phần tử <a> có thuộc tính data-start-price tương ứng với startPrice
        var activePrice = document.querySelector(`.shop__sidebar__price a[data-start-price="${startPrice}"]`);

        if (activePrice) {
            activePrice.classList.add('active'); // Thêm class 'active' vào phần tử tìm được
        }
    }

}

function updateUrl() {
    var url = new URL(window.location.href); // Lấy URL hiện tại
    var activeLabel = document.querySelector('.size-label.active'); // Tìm label đang active

    if (activeLabel) {
        var sizeId = activeLabel.getAttribute('value'); // Lấy giá trị sizeId từ thuộc tính th:value của label
        var currentSizeId = url.searchParams.get('sizeId'); // Lấy sizeId hiện tại từ URL

        if (currentSizeId === sizeId) {
            url.searchParams.delete('sizeId'); // Xóa tham số sizeId nếu nó đang được chọn
            activeLabel.classList.remove('active'); // Bỏ class active khỏi label được chọn
        } else {
            url.searchParams.set('sizeId', sizeId); // Đặt tham số sizeId vào URL nếu chưa có
        }
        url.searchParams.set('page','1')
        window.location.href = url.toString(); // Chuyển hướng tới URL đã cập nhật
    }
}

document.addEventListener('DOMContentLoaded', function () {
    var sizeLabels = document.querySelectorAll('.size-label'); // Lấy tất cả các label size

    sizeLabels.forEach(function (label) {
        label.addEventListener('click', function () {
            sizeLabels.forEach(function (l) {
                l.classList.remove('active'); // Xóa class active khỏi tất cả các label
            });
            label.classList.add('active'); // Thêm class active vào label được click
            updateUrl(); // Cập nhật URL khi có label được chọn
        });
    });

    // Gọi hàm để kiểm tra sizeId từ URL khi trang được load
    updateUrlAndLabel();
});

/* Xử lý khi người dùng chọn màu sắc */
function selectColor(colorId) {
    console.log("Đã chọn màu "+colorId)
    var url = new URL(window.location.href);
    var activeLabel = document.querySelector('.shop__sidebar__color .selected'); // Tìm label đang selected
    console.log(activeLabel)

    if (activeLabel) {
        var currentColorId = url.searchParams.get('colorId'); // Lấy sizeId hiện tại từ URL

        if (colorId.toString()===(currentColorId)) {
            url.searchParams.delete('colorId'); // Xóa tham số sizeId nếu nó đang được chọn
            activeLabel.classList.remove('selected'); // Bỏ class active khỏi label được chọn
        } else {
            url.searchParams.set('colorId', colorId); // Đặt tham số sizeId vào URL nếu chưa có
        }
        url.searchParams.set('page','1')
        window.location.href = url.toString(); // Chuyển hướng tới URL đã cập nhật
    }else {
        url.searchParams.set('colorId', colorId); // Đặt tham số sizeId vào URL nếu chưa có
        url.searchParams.set('page','1')
        window.location.href = url.toString(); // Chuyển hướng tới URL đã cập nhật
    }
}

//Xử lý khi ngươ dùng tìm kiếm tên sản phẩm
let searchInputFormEl = document.getElementById("searchForm");
searchInputFormEl.addEventListener("submit", async (event)=> {
    event.preventDefault(); // Ngăn chặn hành động mặc định của form (submit)

    let inputVal = document.getElementById('searchInput').value.trim();
    console.log(inputVal);
    var url = new URL(window.location.href);
    url.searchParams.set('nameKeyword', inputVal);
    url.searchParams.set('page','1')
    window.location.href = url.toString();
});

//Xử lý khi chuyển trang
function changePage(page){
    console.log(page)
    var url = new URL(window.location.href);
    url.searchParams.set('page',page)
    window.location.href = url.toString();
}

//Xử lý sort
$(document).ready(function() {
    // Lắng nghe sự kiện khi select box thay đổi
    $('#sortSelect').on('change', function() {
        // Lấy giá trị của option được chọn
        var selectedValue = $(this).val();
        var url = new URL(window.location.href);
        url.searchParams.set('sortProduct', selectedValue);
        window.location.href = url.toString();
    });

    // Kiểm tra và thiết lập giá trị cho dropdown khi trang được tải lại
    var urlParams = new URLSearchParams(window.location.search);
    var sortProduct = urlParams.get('sortProduct');
    if (sortProduct !== null) {
        $('#sortSelect').val(sortProduct);
        // Cập nhật hiển thị của dropdown nice-select
        $('#sortSelect').niceSelect('update');
    }
});

//Xử lý giá
const changePrice = (id) => {
    // Tạo đối tượng URL từ URL hiện tại của trang
    var url = new URL(window.location.href);

    // Lấy phần tử <a> có id tương ứng với giá trị id
    var element = document.getElementById(`price-${id}`);

    // Lấy thuộc tính dữ liệu từ phần tử
    var startPrice = element.getAttribute('data-start-price');
    var endPrice = element.getAttribute('data-end-price');

    // Tìm phần tử hiện tại có class 'active'
    var activePrice = document.querySelector('.shop__sidebar__price .active');

    // Kiểm tra nếu có phần tử hiện tại với class 'active'
    if (activePrice) {
        var currentStartPrice = url.searchParams.get('startPrice');

        if (startPrice === currentStartPrice) {
            // Xóa các tham số URL nếu giá startPrice hiện tại trùng với thuộc tính startPrice của phần tử
            url.searchParams.delete('startPrice');
            url.searchParams.delete('endPrice');

            // Gỡ bỏ class 'active' từ phần tử đang được click
            activePrice.classList.remove('active');

            // Cập nhật URL
            url.searchParams.set('page', '1');
            window.location.href = url.toString();
        } else {
            // Nếu không trùng, cập nhật URL với startPrice và endPrice
            if (startPrice) {
                url.searchParams.set('startPrice', startPrice);
            }
            if (endPrice) {
                url.searchParams.set('endPrice', endPrice);
            }

            // Thêm class 'active' vào phần tử đang được click
            element.classList.add('active');

            // Cập nhật URL
            url.searchParams.set('page', '1');
            window.location.href = url.toString();
        }
    } else {
        // Nếu không có phần tử với class 'active', thiết lập các tham số URL
        if (startPrice) {
            url.searchParams.set('startPrice', startPrice);
        }
        if (endPrice) {
            url.searchParams.set('endPrice', endPrice);
        }

        // Thêm class 'active' vào phần tử đang được click
        element.classList.add('active');

        // Cập nhật URL
        url.searchParams.set('page', '1');
        window.location.href = url.toString();
    }
};

// Hàm để lấy tham số từ URL
function getQueryParams() {
    const params = new URLSearchParams(window.location.search);
    const queryParams = {};
    for (const [key, value] of params.entries()) {
        queryParams[key] = value;
    }
    return queryParams;
}

// Kiểm tra các tham số và điều chỉnh hiển thị nút
function checkParamsAndToggleButton() {
    const params = getQueryParams();
    const excludeParams = ['page', 'pageSize', 'sortProduct'];
    const hasOtherParams = Object.keys(params).some(param => !excludeParams.includes(param));

    const btnClearSort = document.getElementById('btnClearSort');
    if (hasOtherParams) {
        btnClearSort.classList.remove('hidden');
    } else {
        btnClearSort.classList.add('hidden');
    }

    // Lưu giá trị của sortProduct nếu có
    const sortProduct = params['sortProduct'];
    btnClearSort.setAttribute('data-sortProduct', sortProduct || '');
}

// Xử lý sự kiện nhấp chuột cho nút "Bỏ chọn"
document.getElementById('btnClearSort').addEventListener('click', function() {
    const sortProduct = this.getAttribute('data-sortProduct');
    let baseUrl = 'http://localhost:8080/product-shop';
    if (sortProduct) {
        baseUrl += `?sortProduct=${encodeURIComponent(sortProduct)}`;
    }
    window.location.href = baseUrl;
});

// Gọi hàm khi trang đã được tải
window.onload = checkParamsAndToggleButton;