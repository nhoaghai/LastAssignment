const toggleFavorite = async (id)=>{
    if (favorites.some(favorite => favorite.product.id === productId)) {
        await deleteFavorite(id)
    } else {
        await createFavorite()
    }
}
const btnFavoriteGroup =document.querySelector(".btnFavoriteGroup")

const renderFavorites = favorites =>{
    let html = "";
    if (favorites.some(favorite => favorite.product.id === productId)) {
        html = `
            <a id="favoriteButton" class="favorite-btn" onclick="toggleFavorite(productId)" style=" cursor: pointer; color: red"><i class="fa fa-heart"></i>  Bỏ yêu thích</a>
        `;
    } else {
        html = `
            <a id="favoriteButton" class="favorite-btn" onclick="toggleFavorite()" style="cursor: pointer;"><i class="fa fa-heart"></i>  Yêu thích</a>
        `;
    }
    btnFavoriteGroup.innerHTML = html;
}

const createFavorite = async () =>{
    const data = {
        userId: userId,
        productId: productId
    }
    console.log(userId)
    console.log(productId)
    try {
        let res = await axios.put("/api/favorites", data);
        toastr.success("Đã thêm vào danh sách yêu thích thành công")
        favorites.unshift(res.data)
        console.log(favorites)
        renderFavorites(favorites)

    } catch (e) {
       console.log(e)
        toastr.error(e.response.data.message);
    }

}

const deleteFavorite = async (id) =>{
    const confirm = window.confirm("Bạn có chắc chắn muốn xóa không")
    if(confirm){
        try {
            const deleteFavor = await axios.delete(`/api/favorites/${id}`)
            console.log("Sự kiện xóa Favor")
            favorites=favorites.filter(f=>f.product.id !==id)
            console.log(favorites)
            renderFavorites(favorites)
            toastr.success("Xóa khỏi danh sách yêu thích thành công")
        } catch (e) {
            console.log(e)
            toastr.error(e.response.data.message);
        }
    }
}
const btnFavoriteAnnotation=document.getElementById("favoriteButtonAnnotation");
if (btnFavoriteAnnotation){
    btnFavoriteAnnotation.addEventListener("click", ()=>{
        toastr.error("Cần phải đăng nhập mới thêm được vào danh sách yêu thích")
    })
}