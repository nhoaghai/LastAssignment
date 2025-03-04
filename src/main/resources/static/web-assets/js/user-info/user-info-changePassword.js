$('#form-changePassword').validate({
    rules: {
        oldPassword: {
            required: true,
        },
        password: {
            required: true,
        },
        confirmPassword:{
            required:true,
            equalTo: "#input-password",
        }
    },
    messages: {
        oldPassword: {
            required: "Mật khẩu cũ không được để trống",
        },
        password: {
            required: "Mật khẩu mới không được để trống",
        },
        confirmPassword:{
            required:"Mật khẩu không được để trống",
            equalTo: "Mật khẩu xác nhận không giống với mật khẩu mới",
        }
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
let  passwordEl = document.getElementById("input-password");
let confirmPasswordEl = document.getElementById("input-confirm-password")
let oldPasswordEl = document.getElementById("input-old-password")

document.getElementById("form-changePassword").addEventListener("submit", async (e)=>{
    e.preventDefault();

    if (!$('#form-changePassword').valid()){
        return;
    }
    const data = {
        oldPassword: oldPasswordEl.value,
        newPassword: passwordEl.value,
        confirmPassword:confirmPasswordEl.value,
    }

    // Gọi API
    try {
        let res = await axios.put(`/api/auth/update-password/${user.id}`, data);
        oldPasswordEl.value=""
        passwordEl.value=""
        confirmPasswordEl.value=""
        toastr.success("Cập nhật thành công")
    } catch (e) {
        console.log(e)
        toastr.error(e.response.data.message);
    }
})