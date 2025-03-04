// Xử lý nút thêm vào giỏ nhg ko đăng nập
const annotationCart=()=>{
    toastr.error("Cần phải đăng nhập mới thêm được vào giỏ hàng")
}

// Khai báo các biến toàn cục
var colorId = null;
var sizeId = null;
var quantity = 1; // Giá trị mặc định của số lượng

// Xử lý sự kiện thay đổi số lượng
$('.pro-qty').on('click', '.qtybtn', function() {
    var $button = $(this);
    var $input = $button.siblings('input');
    quantity = parseFloat($input.val());

    if ($button.hasClass('inc')) {
        quantity++;
    } else {
        quantity = quantity > 1 ? quantity - 1 : 1;
    }

    $input.val(quantity);
    console.log('Quantity:', quantity);
});

// Xử lý sự kiện chọn size
$('.product__details__option__size label').on('click', function() {
    sizeId = $(this).data('size-id'); // Lấy sizeId từ thuộc tính data
    console.log('Size ID:', sizeId);
});

// Xử lý sự kiện chọn màu
$('.product__details__option__color label').on('click', function() {
    $('.product__details__option__color label').removeClass('selected');
    $(this).addClass('selected')
    colorId = $(this).data('color-id');
    console.log('color ID:', colorId);
    changeImage(colorId,productId)
});


//tạo ra cart
const createCart= async ()=>{
    if (colorId==null){
        toastr.error("Bạn chưa chọn màu")
        return

    }
    if (sizeId==null){
        toastr.error("Bạn chưa chọn kích thước")
        return
    }
    if (!stockMap[`${colorId}_${sizeId}`]){
        toastr.error("Sản phẩm hiện hết màu và kích thước này")
        return
    }
    if (quantity> stockMap[`${colorId}_${sizeId}`]){
        toastr.error("Bạn chọn số lượng nhiều hơn số lượng còn lại trong kho")
        return
    }
    const data = {
        userId: userId,
        productId: productId,
        colorId: colorId,
        sizeId: sizeId,
        quantity: quantity
    }
    try {
        let res =await axios.put("/api/carts",data)
        console.log(res)
        // Tạo hiệu ứng cho liên kết giỏ hàng
        const cartLink = document.getElementById('cart-link');
        console.log(cartLink)
        cartLink.classList.remove('text-black')
        cartLink.classList.add('highlight');

        // Xóa lớp CSS sau khi hiệu ứng kết thúc
        setTimeout(() => {
            cartLink.classList.remove('highlight');
            cartLink.classList.add('text-black')
        }, 1000);

        toastr.success("Thêm vào giỏ thành công")


    }catch (e){
       console.log(e)
        toastr.error(e.response.data.message);
    }
}

/*Gọi api*/
const changeImage = async (colorId, productId) =>{
    try {
        const changeImg = await axios.get(`/api/images/${colorId}/${productId}`)
        console.log("Sự kiện đổi ảnh")
        console.log(changeImg.data)
        renderImages(changeImg.data)
    } catch (e) {
        console.log(e)
        toastr.error(e.response.data.message);
    }
};

//Render lại ảnh khi ấn màu
let imgListEl = document.querySelector(".img-list");
let imgListTabEl = document.querySelector(".img-list-tab")
const renderImages = images =>{
    let html1 = "";
    let html2 = "";
    images.forEach((img,index) =>{
        if (index===0){
            html1+=`
                           <li class="nav-item">
                                    <a class="nav-link active" data-toggle="tab" href="#tabs-${index}" role="tab">
                                        <div class="product__thumb__pic set-bg rounded-4" data-setbg="${img.imgUrl}" style="background-image: url('${img.imgUrl}');">
                                        </div>
                                    </a>
                                </li>
                        `
            html2+=`
                         <div class="tab-pane active" id="tabs-${index}" role="tabpanel">
                                    <div class="product__details__pic__item ">
                                        <img class="rounded-4" src="${img.imgUrl}" alt="">
                                    </div>
                                </div>
                        `
        }else {
            html1+=`
                          <li class="nav-item">
                                    <a class="nav-link" data-toggle="tab" href="#tabs-${index}" role="tab">
                                        <div class="product__thumb__pic set-bg rounded-4" data-setbg="${img.imgUrl}" style="background-image: url('${img.imgUrl}');">
                                        </div>
                                    </a>
                                </li>
                       `
            html2+=`
                         <div class="tab-pane" id="tabs-${index}" role="tabpanel">
                                    <div class="product__details__pic__item ">
                                        <img class="rounded-4" src="${img.imgUrl}" alt="">
                                    </div>
                                </div>
                        `
        }
    })
    imgListEl.innerHTML=html1;
    imgListTabEl.innerHTML=html2;
};