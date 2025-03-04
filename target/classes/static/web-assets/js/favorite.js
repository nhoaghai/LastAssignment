//Xử lý nút favorite khi chưa đăng nhập
const attachFavoriteButtonEvents = () => {
    document.querySelectorAll('.push-favorite').forEach(function(button) {
        button.addEventListener('click', function(event) {
            event.preventDefault(); // Ngăn chặn hành vi mặc định của thẻ <a>
            // Lấy productId từ thuộc tính data
            var productId = button.getAttribute('data-product-id');
            console.log('Pushing favorite for product ID ' + productId);

            toggleFavoriteBtn(productId);
        });
    });
};

// Hàm toggleFavoriteBtn
const toggleFavoriteBtn = async (id) => {
    console.log(favoriteProductIds);
    console.log(id);
    if (favoriteProductIds.some(favoriteProductId => favoriteProductId == id)) {
        await deleteFavoriteBtn(id);
    } else {
        await createFavoriteBtn(id);
    }
};

// Hàm createFavoriteBtn
const createFavoriteBtn = async (id) => {
    const data = {
        userId: userId,
        productId: id
    };
    try {
        let res = await axios.put("/api/favorites", data);
        toastr.success("Đã thêm vào danh sách yêu thích thành công");
        favoriteProductIds.unshift(res.data.product.id);
        console.log(favoriteProductIds);
        renderFavoritesBtn(favoriteProductIds);
    } catch (e) {
        console.log(e);
        toastr.error(e.response.data.message);
    }
};

// Hàm deleteFavoriteBtn
const deleteFavoriteBtn = async (id) => {
    const confirm = window.confirm("Bạn có chắc chắn muốn xóa không");
    if (confirm) {
        try {
            await axios.delete(`/api/favorites/${id}`);
            console.log("Sự kiện xóa Favor");
            favoriteProductIds = favoriteProductIds.filter(favProductId => favProductId != id);
            console.log(favoriteProductIds);
            renderFavoritesBtn(favoriteProductIds);
            var currentUrl = window.location.pathname;
            if (currentUrl.includes('/favorite')){
                removeProductFromHtml(id);
            }
            toastr.success("Xóa khỏi danh sách yêu thích thành công");
        } catch (e) {
            console.log(e);
            toastr.error(e.response.data.message);
        }
    }
};

const removeProductFromHtml = (id) => {
    // Tìm phần tử sản phẩm có id tương ứng và xóa nó khỏi DOM
    console.log("Xóa item")
    document.querySelectorAll('.product__item').forEach(item => {
        const productId = item.querySelector('.push-favorite').getAttribute('data-product-id');
        if (productId == id) {
            item.parentElement.remove(); // Xóa sản phẩm khỏi HTML
        }
    });
};

// Hàm renderFavoritesBtn
const renderFavoritesBtn = (favoriteProductIds) => {
    const btnFavorite = document.querySelectorAll(".btnFavorite");
    btnFavorite.forEach(btn => {
        let productId = btn.querySelector("button").getAttribute('data-product-id');
        let html = "";
        if (favoriteProductIds.some(favoriteProductId => favoriteProductId == productId)) {
            html = `
                    <button class="border-0 push-favorite bg-white text-center pt-2 pb-2 pl-3 pr-3" data-product-id="${productId}">
                        <i class="fas fa-heart text-danger"></i>
                    </button>
                `;
        } else {
            html = `
                    <button class="border-0 push-favorite bg-white text-center pt-2 pb-2 pl-3 pr-3" data-product-id="${productId}">
                        <i class="far fa-heart text-danger"></i>
                    </button>
                `;
        }
        btn.innerHTML = html;
    });

    attachFavoriteButtonEvents(); // Gán sự kiện click cho các nút sau khi cập nhật HTML
};

// Khởi tạo sự kiện cho các nút yêu thích khi trang được tải
attachFavoriteButtonEvents();

// Xử lý nút favorite khi chưa đăng nhập
const btnFavoriteAnnotationClass = document.querySelectorAll(".favoriteButtonAnnotation");
if (btnFavoriteAnnotationClass) {
    btnFavoriteAnnotationClass.forEach(btn => {
        btn.addEventListener("click", () => {
            toastr.error("Cần phải đăng nhập mới thêm được vào danh sách yêu thích");
        });
    });
}