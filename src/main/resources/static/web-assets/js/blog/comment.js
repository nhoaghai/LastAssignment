// render comment
const formatDate = dateStr => {
    const date = new Date(dateStr);
    const day = `0${date.getDate()}`.slice(-2); // 01 -> 01, 015 -> 15
    const month = `0${date.getMonth() + 1}`.slice(-2);
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
}
const commentListEl = document.querySelector(".comment-list");
const commentSizeEl = document.getElementById("comments-size")
const renderComment = (comments) => {
    let html = "";
    comments.forEach((comment) => {
        const isCurrentUser = userId !== null && userId === comment.user.id;
        html += `
            <div class="comment-info bg-light-subtle rounded-5 p-3 mt-3" style="background-color: #f3f2ee">
                <div class="d-flex align-items-center">
                    <img src="${comment.user.avatar}" alt="Avatar" class="rounded-circle" style="width: 50px; height: 50px;">
                    <div class="mt-2 ml-2">
                        <h5 class="ml-1 mt-1">${comment.user.name}</h5>
                    </div>
                    <div class="ml-auto">
                        <p class="pt-2 ms-2 text-body-tertiary">${formatDate(comment.createdAt)}</p>
                    </div>
                </div>
                <div class="comment-content ps-1 pt-2">
                    <p>${comment.content}</p>
                </div>
                ${isCurrentUser ? `
                    <div class="mt-2">
                        <button onclick="openEditModal(${comment.id})" style="border: none">Sửa</button>
                        <button onclick="deleteCmt(${comment.id})" style="border: none">Xóa</button>
                    </div>
                ` : ''}
                <hr>
            </div>
        `;
    });
    commentSizeEl.innerText = `${comments.length} Comments`;
    commentListEl.innerHTML = html;
}


// Tạo comment
const formCommentEl = document.getElementById("form-comment");
const commentContentEl = document.getElementById("comment-content");
const modalCommentEl = document.getElementById("comment-modal");
let idCommentEdit = null;
const myModalCommentEl = new bootstrap.Modal(modalCommentEl, {
    keyboard: false
})
// reset
modalCommentEl.addEventListener('hidden.bs.modal', event => {
    console.log("Su kien dong modal")
    modalCommentTitleEl.innerText="Tạo bình luận"
    btnCreateCommentEl.innerText="Tạo";
    commentContentEl.value = "";
    idCommebtEdit=null;
})


formCommentEl.addEventListener("submit", async (e) => {
    e.preventDefault();
    console.log("Đã nghe ấn nút")
    // TODO: Validate các thông tin (sử dụng thư jQuery Validation)

    $('#form-comment').valid();

    if (!$('#form-comment').valid()){
        return;
    }

    const data = {
        content: commentContentEl.value,
        blogId: blogId
    }

    // Gọi API
    if (idCommentEdit!=null){
        await updateCmt(data);
    }else {
        await postCmt(data);
    }
})
const postCmt =async (data) => {
    try {
        let res = await axios.post("/api/comments", data);
        comments.unshift(res.data);
        renderComment(comments);

        // Dong modal
        myModalCommentEl.hide();
        toastr.success("Bình luận thành công")


    } catch (e) {
        toastr.error("Lỗi: "+e)
    }
}

//Nút sửa, xóa
const deleteCmt =async (id)=>{
    const confirm = window.confirm("Bạn có chắc chắn muốn xóa không")
    if(confirm){
        try {
            const deleteCmt = await axios.delete(`/api/comments/${id}`)
            console.log("Sự kiện xóa rv")
            comments=comments.filter(cmt=>cmt.id !==id);
            renderComment(comments)

            // Dong modal
            myModalCommentEl.hide();
            toastr.success("Xóa thành công bình luận")
        } catch (e) {
            toastr.error(e.response.data.message)
        }
    }
}
const modalCommentTitleEl = document.querySelector(".modal-title")
const btnCreateCommentEl = document.getElementById("btn-create-comment")

const openEditModal = (id) => {
    const commentToEdit = comments.find(comment => comment.id === id);
    if (commentToEdit) {
        myModalCommentEl.show();
        commentContentEl.value = commentToEdit.content;
        modalCommentTitleEl.innerText = "Sửa bình luận";
        btnCreateCommentEl.innerText = "Sửa";
        idCommentEdit = id;
    } else {
        console.error("Comment with id " + id + " not found.");
    }
}
const updateCmt=async (data) =>{
    try {
        let updateData = await axios.put(`/api/comments/${idCommentEdit}`, data);

        const editedCommentIndex = comments.findIndex(comment => comment.id === idCommentEdit);
        if (editedCommentIndex !== -1) {
            comments[editedCommentIndex] = updateData.data;
            console.log(updateData.data)
            renderComment(comments);
        }

        // Dong modal
        myModalCommentEl.hide();
        toastr.success("Cập nhật thành công")
    } catch (e) {
        toastr.error(e.response.data.message)
    }
}
$('#form-comment').validate({
    rules: {
        content: {
            required: true,
        },
    },
    messages: {
        content: {
            required: "Không được để trống nội dung",
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
