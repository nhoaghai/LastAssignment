let selectedUserId="";
let addresses;
let province =""
//USER
// Mở modal cho người dùng
document.getElementById('btnModalUser').addEventListener('click', function () {
    document.getElementById('user-select').value=selectedUserId;
    $('#modalUser').modal('show');
});
// Xử lý sự kiện chọn người dùng
document.getElementById('btnSelectUser').addEventListener('click', async function () {
    selectedUserId = document.getElementById('user-select').value;
    console.log('Người dùng đã chọn:', selectedUserId);
    if (selectedUserId!=""){
        document.getElementById("userChosen").innerHTML = `<strong>User đang được chọn là: ${users.find(u => u.id == selectedUserId).name}</strong>`
        document.getElementById("email").value=users.find(u => u.id == selectedUserId).email;
        try {
            let res = await axios.get(`/api/admin/addresses/user/${selectedUserId}`)
            addresses=res.data
            renderAddressModal(addresses);
            document.getElementById("btnModalAddress").disabled=false;
            $('#modalUser').modal('hide');
        } catch (e) {
            toastr.error(e.response.data.message)
        }
    }else {
        document.getElementById("userChosen").innerHTML = "<strong>User đang được chọn là: </strong>"
        document.getElementById("btnModalAddress").disabled=true;
        document.getElementById("form-order").reset()
        $('#modalUser').modal('hide');
    }
});