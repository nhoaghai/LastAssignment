// Validate và gọi api
$('#form-updateInfo').validate({
    rules: {
        name: {
            required:true,
        },
    },
    messages: {
        name: {
            required: "Tên tài khoản không được để trống",
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
$('#form-updateInfo').on('submit', async (e) => {
    e.preventDefault();
    console.log("Form submission attempted");
    if (!$('#form-updateInfo').valid()){
        return;
    }

    const data = {
        userId: user.id,
        name: $('#input-name').val(),
    };

    try {
        let res = await axios.put("/api/auth/updateInfo", data);
        toastr.success("Thay đổi thông tin thành công");
        let html ="";

        document.querySelectorAll('.dropdown').forEach(e=>{

            html+=`
                      <a class="btn bg-dark dropdown-toggle nav-link text-white" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        ${res.data.name}
                    </a>
                    <ul class="dropdown-menu">
                        ${res.data.role.toString() === 'ADMIN' ? `
                        <li>
                            <a class="dropdown-item text-black" href="/admin/dashboard">Trang quản trị</a>
                        </li>` : ''}
                        <li>
                            <a class="dropdown-item text-black" href="/user-info">Thông tin cá nhân</a>
                        </li>
                        <li>
                            <a class="dropdown-item text-black" href="#">Lịch sử mua hàng</a>
                        </li>
                        <li>
                            <a class="dropdown-item text-black" href="/logout">Đăng xuất</a>
                        </li>
                    </ul>
                    `
            e.innerHTML=html;
            html="";
        })
    } catch (e) {
        console.log(e);
        toastr.error(e.response.data.message);
    }
});

const avatarPreview = document.getElementById("avatar")
const btnUpdateAvatarEl = document.getElementById("btnUpdateAvatar");
btnUpdateAvatarEl.addEventListener('change', (e) => {
    const file = e.target.files[0];

    // create form data with key file and value is file in input
    const formData = new FormData();
    formData.append('file', file);

    axios.post(`/api/auth/${user.id}/update-avatar`, formData)
        .then(res => {
            if (res.status === 200) {
                avatarPreview.src = res.data;
                toastr.success('Cập nhật avatar thành công');
            }
        })
        .catch(e => {
            console.log(e)
            toastr.error(e.response.data.message);
        });
});