// Lấy thông tin địa chỉ để lắp vào bảng
$(document).ready(function() {
    const apiCities = 'https://esgoo.net/api-tinhthanh/1/0.htm';
    const apiDistricts = idProvince => `https://esgoo.net/api-tinhthanh/2/${idProvince}.htm`;
    const apiWards = idDistrict => `https://esgoo.net/api-tinhthanh/3/${idDistrict}.htm`;

    let provincesData = {};
    let districtsData = {};
    let wardsData = {};

    // Function to fetch provinces
    async function fetchProvinces() {
        try {
            const response = await axios.get(apiCities);
            provincesData = response.data.data.reduce((acc, province) => {
                acc[province.id] = province.name;
                return acc;
            }, {});
        } catch (error) {
            console.error('Error fetching provinces:', error);
        }
    }

    // Function to fetch districts
    async function fetchDistricts(provinceId) {
        try {

            const response = await axios.get(apiDistricts(provinceId));

            districtsData[provinceId] = response.data.data.reduce((acc, district) => {
                acc[district.id] = district.name;
                return acc;
            }, {});

        } catch (error) {
            console.error(`Error fetching districts for province ${provinceId}:`, error);
        }
    }

    // Function to fetch wards
    async function fetchWards(districtId) {
        try {
            const response = await axios.get(apiWards(districtId));
            wardsData[districtId] = response.data.data.reduce((acc, ward) => {
                acc[ward.id] = ward.name;
                return acc;
            }, {});
        } catch (error) {
            console.error(`Error fetching wards for district ${districtId}:`, error);
        }
    }

    // Function to replace IDs with names
    async function replaceAddressIdsWithNames() {
        try {
            await fetchProvinces(); // Fetch provinces first

            $('p.address-w-d-p').each(async function () {
                const text = $(this).text();
                const [wardId, districtId, provinceId] = text.split(' - ').map(part => part.trim());

                // Ensure districts are fetched
                if (provincesData[provinceId]) {
                    await fetchDistricts(provinceId);
                }

                // Ensure wards are fetched
                if (districtsData[provinceId] || districtsData[provinceId][districtId]) {
                    await fetchWards(districtId);
                }

                // Get names
                const wardName = wardsData[districtId] ? wardsData[districtId][wardId] || '' : '';
                const districtName = districtsData[provinceId] ? districtsData[provinceId][districtId] || '' : '';
                const provinceName = provincesData[provinceId] || '';


                $(this).text(`${wardName} - ${districtName} - ${provinceName}`);
            });
        } catch (error) {
            console.error('Error replacing address IDs with names:', error);
        }
    }

    // Call replaceAddressIdsWithNames after data is fetched
    replaceAddressIdsWithNames();
});

//Lấy thông tin về địa chỉ
$(document).ready(function() {
    const apiCities = 'https://esgoo.net/api-tinhthanh/1/0.htm';
    const apiDistricts = idProvince => `https://esgoo.net/api-tinhthanh/2/${idProvince}.htm`;
    const apiWards = idDistrict => `https://esgoo.net/api-tinhthanh/3/${idDistrict}.htm`;

    axios.get(apiCities)
        .then(response => {
            const provinceSelect = document.getElementById('input-province');
            response.data.data.forEach(province => {
                const option = document.createElement('option');
                option.value = province.id;
                option.setAttribute("data-name", province.name);
                option.textContent = province.name;
                provinceSelect.appendChild(option);
            });
        })
        .catch(() => {});

    $('#input-province').on('change', function() {
        const provinceCode = this.value;
        if (provinceCode) {
            axios.get(apiDistricts(provinceCode))
                .then(response => {
                    const districtSelect = document.getElementById('input-district');
                    districtSelect.innerHTML = '<option value="">Chọn Quận/Huyện</option>';
                    response.data.data.forEach(district => {
                        const option = document.createElement('option');
                        option.value = district.id;
                        option.setAttribute("data-name", district.name);
                        option.textContent = district.name;
                        districtSelect.appendChild(option);
                    });
                    districtSelect.disabled = false;
                })
                .catch(() => {});
        } else {
            const districtSelect = document.getElementById('input-district');
            districtSelect.innerHTML = '<option value="">Chọn Quận/Huyện</option>';
            districtSelect.disabled = true;
            document.getElementById('input-ward').innerHTML = '<option value="">Chọn Phường/Xã</option>';
            document.getElementById('input-ward').disabled = true;
        }
    });

    $('#input-district').on('change', function() {
        const districtCode = this.value;
        if (districtCode) {
            axios.get(apiWards(districtCode))
                .then(response => {
                    const wardSelect = document.getElementById('input-ward');
                    wardSelect.innerHTML = '<option value="">Chọn Phường/Xã</option>';
                    response.data.data.forEach(ward => {
                        const option = document.createElement('option');
                        option.value = ward.id;
                        option.setAttribute("data-name", ward.name);
                        option.textContent = ward.name;
                        wardSelect.appendChild(option);
                    });
                    wardSelect.disabled = false;
                })
                .catch(() => {});
        } else {
            document.getElementById('input-ward').innerHTML = '<option value="">Chọn Phường/Xã</option>';
            document.getElementById('input-ward').disabled = true;
        }
    });
});

// Validate và gọi api
$('#form-add-address').validate({
    rules: {
        nameCustomer: {
            required:true,
        },
        phone:{
            required: true,
        },
        province:{
            required: true,
        },
        district:{
            required: true,
        },
        ward:{
            required: true,
        },
        addressDetail:{
            required: true,
        }
    },
    messages: {
        nameCustomer: {
            required:"Tên khách hàng không được để trống",
        },
        phone:{
            required: "Số điện thoại không được để trống",
        },
        province:{
            required: "Không được để trống",
        },
        district:{
            required: "Không được để trống",
        },
        ward:{
            required: "Không được để trống",
        },
        addressDetail:{
            required: "Không được để trống",
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

//Thêm sửa xóa address
//Phần render
// Hàm hiển thị danh sách địa chỉ
let addressListEl = document.getElementById("address-list");

// Biến lưu trữ dữ liệu tỉnh/thành phố, quận/huyện, phường/xã
let provinces = [];
let districts = [];
let wards = [];

// Hàm lấy danh sách tỉnh/thành phố
const getProvinces = () => {
    return axios.get('https://esgoo.net/api-tinhthanh/1/0.htm')
        .then(response => provinces=response.data.data)
        .catch(error => {
            console.error('Lỗi khi lấy danh sách tỉnh/thành phố:', error);
            return [];
        });
}

// Hàm lấy danh sách quận/huyện theo id tỉnh/thành phố
const getDistricts = (provinceId) => {
    return axios.get(`https://esgoo.net/api-tinhthanh/2/${provinceId}.htm`)
        .then(response => districts=response.data.data)
        .catch(error => {
            console.error('Lỗi khi lấy danh sách quận/huyện:', error);
            return [];
        });
}

// Hàm lấy danh sách phường/xã theo id quận/huyện
const getWards = (districtId) => {
    return axios.get(`https://esgoo.net/api-tinhthanh/3/${districtId}.htm`)
        .then(response => wards=response.data.data)
        .catch(error => {
            console.error('Lỗi khi lấy danh sách phường/xã:', error);
            return [];
        });
}

// Hàm lấy tên theo id
const getNameById = (id, list) => {
    const item = list.find(el => el.id== id.toString());
    return item ? item.name : id;
}

// Hàm hiển thị danh sách địa chỉ
const renderAddress = async (addresses) => {
    let html = "";
    for (const address of addresses) {
        await getProvinces()
        await getDistricts(address.province)
        await getWards(address.district)
        const provinceName = getNameById(address.province, provinces);
        const districtName = getNameById(address.district, districts);
        const wardName = getNameById(address.ward, wards);

        html += `
        <tr>
            <td class="align-middle">
                <h5>
                    ${address.receiverName} | ${address.phone}
                    ${address.chosen
            ? '<span><strong>(Mặc định)</strong></span>'
            : `<span style="cursor: pointer" class="text-warning text-decoration-underline" onclick="setAddressChosen(${address.id})">(Đặt địa chỉ làm mặc định)</span>`}
                </h5>
                <p class="m-0">${address.addressDetail}</p>
                <p class="m-0 address-w-d-p">${wardName} - ${districtName} - ${provinceName}</p>
            </td>
            <td class="text-end">
                <button type="button" class="btn btn-warning text-white btn-sm" onclick="openEditModal(${address.id})">
                    <i class="fas fa-pencil-alt"></i>
                </button>
                ${!address.chosen
            ? `<button type="button" class="btn btn-danger btn-sm" onclick="deleteAddress(${address.id})">
                        <i class="fas fa-trash-alt"></i>
                    </button>`
            : ''}
            </td>
        </tr>
        `;
    }
    addressListEl.innerHTML = html;
}

// Hàm khởi tạo dropdowns và lấy dữ liệu từ API
$(document).ready(function() {
    const apiCities = 'https://esgoo.net/api-tinhthanh/1/0.htm';
    const apiDistricts = idProvince => `https://esgoo.net/api-tinhthanh/2/${idProvince}.htm`;
    const apiWards = idDistrict => `https://esgoo.net/api-tinhthanh/3/${idDistrict}.htm`;

    // Lấy danh sách tỉnh/thành phố
    axios.get(apiCities)
        .then(response => {
            const provinceSelect = document.getElementById('input-province');
            response.data.data.forEach(province => {
                const option = document.createElement('option');
                option.value = province.id;
                option.setAttribute("data-name", province.name);
                option.textContent = province.name;
                provinceSelect.appendChild(option);
            });
        })
        .catch(error => console.error('Lỗi khi lấy danh sách tỉnh/thành phố:', error));

    // Xử lý sự kiện thay đổi tỉnh/thành phố
    $('#input-province').on('change', function() {
        const provinceCode = this.value;
        const districtSelect = document.getElementById('input-district');
        const wardSelect = document.getElementById('input-ward');

        if (provinceCode) {
            axios.get(apiDistricts(provinceCode))
                .then(response => {
                    districtSelect.innerHTML = '<option value="">Chọn Quận/Huyện</option>';
                    response.data.data.forEach(district => {
                        const option = document.createElement('option');
                        option.value = district.id;
                        option.setAttribute("data-name", district.name);
                        option.textContent = district.name;
                        districtSelect.appendChild(option);
                    });
                    districtSelect.disabled = false;
                    // Reset ward dropdown
                    wardSelect.innerHTML = '<option value="">Chọn Phường/Xã</option>';
                    wardSelect.disabled = true;
                })
                .catch(error => console.error('Lỗi khi lấy danh sách quận/huyện:', error));
        } else {
            districtSelect.innerHTML = '<option value="">Chọn Quận/Huyện</option>';
            districtSelect.disabled = true;
            wardSelect.innerHTML = '<option value="">Chọn Phường/Xã</option>';
            wardSelect.disabled = true;
        }
    });

    // Xử lý sự kiện thay đổi quận/huyện
    $('#input-district').on('change', function() {
        const districtCode = this.value;
        const wardSelect = document.getElementById('input-ward');

        if (districtCode) {
            axios.get(apiWards(districtCode))
                .then(response => {
                    wardSelect.innerHTML = '<option value="">Chọn Phường/Xã</option>';
                    response.data.data.forEach(ward => {
                        const option = document.createElement('option');
                        option.value = ward.id;
                        option.setAttribute("data-name", ward.name);
                        option.textContent = ward.name;
                        wardSelect.appendChild(option);
                    });
                    wardSelect.disabled = false;
                })
                .catch(error => console.error('Lỗi khi lấy danh sách phường/xã:', error));
        } else {
            wardSelect.innerHTML = '<option value="">Chọn Phường/Xã</option>';
            wardSelect.disabled = true;
        }
    });
});



const nameCustomerEl = document.getElementById("input-nameCustomer");
const phoneEl = document.getElementById("input-phone");
const provinceEl = document.getElementById("input-province");
const districtEl = document.getElementById("input-district");
const wardEl = document.getElementById("input-ward");
const addressDetailEl = document.getElementById("input-addressDetail");
const modalAddAddressEl = document.getElementById("address-modal");
const modalAddressTitleEl = document.querySelector(".modal-title")
const btnCreateAddressEl = document.getElementById("btn-create-address")

let idAddressEdit = null;
const myModalAddAddressEl = new bootstrap.Modal(modalAddAddressEl, {
    keyboard: false
})
const openEditModal = async (id) => {
    try {
        // Hiển thị modal
        myModalAddAddressEl.show();

        // Tìm địa chỉ đã chọn từ danh sách địa chỉ
        let selectedAddress = addresses.find(address => address.id === id);
        let { receiverName, phone, province, district, ward, addressDetail } = selectedAddress;

        // Cập nhật các trường của modal với thông tin địa chỉ đã chọn
        nameCustomerEl.value = receiverName;
        phoneEl.value = phone;
        addressDetailEl.value = addressDetail;

        // Lấy danh sách các tỉnh/thành phố và cập nhật dropdown
        const provincesResponse = await axios.get('https://esgoo.net/api-tinhthanh/1/0.htm');
        const provinces = provincesResponse.data.data;
        populateOptions(provinceEl, provinces, 'Chọn Tỉnh/Thành Phố');

        // Hàm cập nhật district dropdown dựa trên province chọn
        async function updateDistricts(provinceCode) {
            if (provinceCode) {
                try {
                    const districtsResponse = await axios.get(`https://esgoo.net/api-tinhthanh/2/${provinceCode}.htm`);
                    const districts = districtsResponse.data.data;
                    districtEl.disabled = false;
                    populateOptions(districtEl, districts, 'Chọn Quận/Huyện');


                    // Reset ward dropdown
                    wardEl.innerHTML = '<option value="">Chọn Phường/Xã</option>';
                    wardEl.disabled = true;

                    let exists = districts.some(d => d.id == district);

                    // Điền giá trị vào trường quận/huyện nếu có dữ liệu
                    if (exists) {
                        setSelectedValue(districtEl, district);
                        $('#input-district').trigger('change');  // Cập nhật ward sau khi chọn district
                    }else {
                        districtEl.disabled = false;
                        districtEl.innerHTML = '<option value="">Chọn Quận/Huyện</option>';
                        populateOptions(districtEl, districts, 'Chọn Quận/Huyện');
                    }
                } catch (error) {
                    console.error('Lỗi khi lấy danh sách quận/huyện:', error);
                }
            } else {
                districtEl.innerHTML = '<option value="">Chọn Quận/Huyện</option>';
                districtEl.disabled = true;
                wardEl.innerHTML = '<option value="">Chọn Phường/Xã</option>';
                wardEl.disabled = true;
            }
        }

        // Hàm cập nhật ward dropdown dựa trên district chọn
        async function updateWards(districtCode) {
            if (districtCode) {
                try {
                    const wardsResponse = await axios.get(`https://esgoo.net/api-tinhthanh/3/${districtCode}.htm`);
                    const wards = wardsResponse.data.data;
                    wardEl.disabled = false;
                    populateOptions(wardEl, wards, 'Chọn Phường/Xã');

                    let exists = wards.some(w => w.id == ward);

                    // Điền giá trị vào trường phường/xã nếu có dữ liệu
                    if (exists) {
                        setSelectedValue(wardEl, ward);
                    }else {
                        wardEl.disabled = false;
                        wardEl.innerHTML = '<option value="">Chọn Phường/Xã</option>';
                        populateOptions(wardEl, wards, 'Chọn Phường/Xã');
                    }
                } catch (error) {
                    console.error('Lỗi khi lấy danh sách phường/xã:', error);
                }
            } else {
                wardEl.innerHTML = '<option value="">Chọn Phường/Xã</option>';
                wardEl.disabled = true;
            }
        }

        // Xử lý sự kiện khi chọn tỉnh/thành phố
        $('#input-province').off('change').on('change', function() {
            const provinceCode = this.value;
            updateDistricts(provinceCode);
            // Reset district và ward khi tỉnh thay đổi
            districtEl.value = "";
            wardEl.innerHTML = '<option value="">Chọn Phường/Xã</option>';
            wardEl.disabled = true;
        });

        // Xử lý sự kiện khi chọn quận/huyện
        $('#input-district').off('change').on('change', function() {
            const districtCode = this.value;
            updateWards(districtCode);
        });

        // Điền giá trị vào trường tỉnh/thành phố nếu có dữ liệu
        if (province) {
            setSelectedValue(provinceEl, province);
            $('#input-province').trigger('change'); // Cập nhật district và ward
        } else {
            // Nếu không có giá trị province, disable district và ward
            districtEl.innerHTML = '<option value="">Chọn Quận/Huyện</option>';
            districtEl.disabled = true;
            wardEl.innerHTML = '<option value="">Chọn Phường/Xã</option>';
            wardEl.disabled = true;
        }

        // Xử lý việc chọn district và ward sau khi province đã được chọn
        if (district) {
            $('#input-district').trigger('change'); // Cập nhật ward
        }

        // Cập nhật tiêu đề và nút trong modal
        modalAddressTitleEl.innerText = "Sửa địa chỉ";
        btnCreateAddressEl.innerText = "Cập nhật";
        idAddressEdit = id;

    } catch (error) {
        console.error('Lỗi khi mở modal chỉnh sửa:', error);
    }
};

// Hàm tạo các tùy chọn cho các trường select
function populateOptions(selectElement, data, placeholder) {
    selectElement.innerHTML = `<option value="">${placeholder}</option>`;
    data.forEach(item => {
        const option = document.createElement('option');
        option.value = item.id;
        option.textContent = item.name;
        selectElement.appendChild(option);
    });
}

// Hàm điền dữ liệu vào các trường select
function setSelectedValue(selectElement, value) {
    selectElement.value = value;
}

// Khi đóng moadl thì reset
modalAddAddressEl.addEventListener('hidden.bs.modal', event => {
    console.log("Modal đã đóng");

    // Reset tiêu đề và nút
    modalAddressTitleEl.innerText = "Thêm địa chỉ mới";
    btnCreateAddressEl.innerText = "Tạo";

    // Reset các trường nhập liệu
    nameCustomerEl.value = "";
    phoneEl.value = "";
    provinceEl.value = "";
    districtEl.innerHTML = '<option value="">Chọn Quận/Huyện</option>';
    districtEl.disabled = true;
    wardEl.innerHTML = '<option value="">Chọn Phường/Xã</option>';
    wardEl.disabled = true;
    addressDetailEl.value = "";

    // Reset idAddressEdit
    idAddressEdit = null;
    // Hàm cập nhật district dropdown dựa trên province chọn
    async function updateDistricts(provinceCode) {
        if (provinceCode) {
            try {
                const districtsResponse = await axios.get(`https://esgoo.net/api-tinhthanh/2/${provinceCode}.htm`);
                const districts = districtsResponse.data.data;
                populateOptions(districtEl, districts, 'Chọn Quận/Huyện');
                districtEl.disabled = false;

                // Reset ward dropdown
                wardEl.innerHTML = '<option value="">Chọn Phường/Xã</option>';
                wardEl.disabled = true;
            } catch (error) {
                console.error('Lỗi khi lấy danh sách quận/huyện:', error);
            }
        } else {
            districtEl.innerHTML = '<option value="">Chọn Quận/Huyện</option>';
            districtEl.disabled = true;
            wardEl.innerHTML = '<option value="">Chọn Phường/Xã</option>';
            wardEl.disabled = true;
        }
    }

    // Hàm cập nhật ward dropdown dựa trên district chọn
    async function updateWards(districtCode) {
        if (districtCode) {
            try {
                const wardsResponse = await axios.get(`https://esgoo.net/api-tinhthanh/3/${districtCode}.htm`);
                const wards = wardsResponse.data.data;
                populateOptions(wardEl, wards, 'Chọn Phường/Xã');
                wardEl.disabled = false;
            } catch (error) {
                console.error('Lỗi khi lấy danh sách phường/xã:', error);
            }
        } else {
            wardEl.innerHTML = '<option value="">Chọn Phường/Xã</option>';
            wardEl.disabled = true;
        }
    }

    // Xử lý sự kiện khi chọn tỉnh/thành phố
    $('#input-province').off('change').on('change', function() {
        const provinceCode = this.value;
        updateDistricts(provinceCode);
        districtEl.value = "";  // Reset district và ward khi tỉnh thay đổi
        wardEl.innerHTML = '<option value="">Chọn Phường/Xã</option>';
        wardEl.disabled = true;
    });

    // Xử lý sự kiện khi chọn quận/huyện
    $('#input-district').off('change').on('change', function() {
        const districtCode = this.value;
        updateWards(districtCode);
    });
})


$('#form-add-address').on('submit', async (e) => {
    e.preventDefault();
    console.log("Form submission attempted");
    if (!$('#form-add-address').valid()){
        return;
    }

    const data = {
        userId: user.id,
        receiverName: $('#input-nameCustomer').val(),
        phone: $('#input-phone').val(),
        province: document.getElementById('input-province').value,
        district: document.getElementById('input-district').value,
        ward: document.getElementById('input-ward').value,
        addressDetail: $('#input-addressDetail').val(),
    };
    if (idAddressEdit!=null){
        await updateAddress(data);
    }else {
        await postAddress(data);
    }
});

//Taọ dịa chỉ
const postAddress =async (data) => {
    try {
        let res = await axios.post("/api/addresses", data);
        addresses=res.data;
        await renderAddress(res.data);
        // Dong modal
        myModalAddAddressEl.hide();
        toastr.success("Thêm địa chỉ mới thành công")
    } catch (e) {
        toastr.error("Lỗi: "+error)
    }
}

//Sửa địa chỉ
const updateAddress=async (data) =>{
    try {
        let res = await axios.put(`/api/addresses/${idAddressEdit}`, data);
        addresses=res.data
        await renderAddress(res.data);
        // Dong modal
        myModalAddAddressEl.hide();
        toastr.success("Cập nhật thành công địa chỉ")
    } catch (e) {
        toastr.error("Lỗi: "+error)
    }
}

// Xóa địa chỉ
const deleteAddress =async (id)=>{
    const confirm = window.confirm("Bạn có chắc chắn muốn xóa không")
    if(confirm){
        try {
            const res = await axios.delete(`/api/addresses/${id}/user/${user.id}`)
            console.log("Sự kiện xóa address")
            addresses=res.data
            await renderAddress(res.data);
            // Dong modal
            myModalAddAddressEl.hide();
            toastr.success("Xóa thành công địa chỉ")
        } catch (error) {
            toastr.error("Lỗi: "+error)
        }
    }
}

//Đặt address mặc định
const setAddressChosen=async (id)=>{
    try {
        let res = await axios.put(`/api/addresses/updateChosen/${id}/user/${user.id}`)
        renderAddress(res.data)
        toastr.success("Đã đổi địa chỉ mặc định thành công!")
    }catch (e){
        console.log(e)
        toastr.error(e.response.data.message);
    }
}