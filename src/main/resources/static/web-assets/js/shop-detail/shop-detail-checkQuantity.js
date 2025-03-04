const sizes = document.querySelectorAll('.product__details__option__size label');
const colors = document.querySelectorAll('.product__details__option__color input[type="radio"]');
const stockStatus = document.getElementById('stock-status');
const stockQuantity = document.getElementById('stock-quantity');
const sizeLabels = document.querySelectorAll('.size-label');
const colorLabels = document.querySelectorAll('.color-label');
document.addEventListener('DOMContentLoaded', function () {

    // Cập nhật trạng thái sẵn có ban đầu
    updateInitialAvailability();


    function updateInitialAvailability() {


        sizeLabels.forEach(sizeLabel => {
            const sizeId = sizeLabel.dataset.sizeId;
            const available = Array.from(colors).some(color => {
                const colorId = color.value;
                console.log( stockMap[`${colorId}_${sizeId}`] > 0)
                return stockMap[`${colorId}_${sizeId}`] > 0;
            });
            if (!available) {
                sizeLabel.classList.add('unavailable');
                sizeLabel.querySelector('input').disabled = true;
            }
        });

        colorLabels.forEach(colorLabel => {
            const colorId = colorLabel.dataset.colorId;

            const available = Array.from(sizeLabels).some(sizeLabel => {
                const sizeId = sizeLabel.dataset.sizeId
                console.log(sizeId)
                console.log(`${colorId}_${sizeId}`)
                return stockMap[`${colorId}_${sizeId}`] > 0;
            });
            if (!available) {
                colorLabel.classList.add('unavailable');
                colorLabel.querySelector('input').disabled = true;
            }
        });
    }


});
$('.product__details__option__size label').on('click', function() {
    Array.from(colors).forEach(color => {
        let color_id = color.value;
        console.log(color_id);
        if (stockMap[`${color_id}_${sizeId}`]==null){
            color.parentElement.classList.add('unavailable');
        }else {
            color.parentElement.classList.remove('unavailable');
        }
    });
    if (colorId!=null && sizeId!=null){
        updateStockStatus(colorId, sizeId);
    }
});

$('.product__details__option__color label').on('click', function() {
    Array.from(sizeLabels).forEach(sizeLabel => {
        const size_id = sizeLabel.dataset.sizeId
        if (stockMap[`${colorId}_${size_id}`]==null){
            sizeLabel.classList.add('unavailable');
        }else {
            sizeLabel.classList.remove('unavailable');
        }
    });
    if (colorId!=null && sizeId!=null){
        updateStockStatus(colorId, sizeId);
    }
});

function updateStockStatus(colorId, sizeId) {
    const stockKey = `${colorId}_${sizeId}`;
    const quantity = stockMap[stockKey] || 0;

    if (quantity > 0) {
        stockStatus.textContent = 'Còn hàng';
        stockQuantity.textContent = `Số lượng: ${quantity}`;
    } else {
        stockStatus.textContent = 'Hết hàng';
        stockQuantity.textContent = '';
    }
}