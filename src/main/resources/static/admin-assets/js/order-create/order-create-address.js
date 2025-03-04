const districtSelect = document.getElementById('district');
const wardSelect = document.getElementById('ward');
const addressDetailInput = document.getElementById('addressDetail');
const nameCustomerInput = document.getElementById('nameCustomer');
const phoneInput = document.getElementById('phone');

//Render Address Modal
function renderAddressModal(addresses) {
    const addressList = document.getElementById('address-list');
    addressList.innerHTML = ''; // Clear any existing content

    addresses.forEach(address => {
        const tr = document.createElement('tr');

        tr.innerHTML = `
                <td class="align-middle">
                    <h5>${address.receiverName} | ${address.phone}&nbsp;
                        ${address.chosen ? '<strong>(Mặc định)</strong>' : ''}
                    </h5>
                    <p class="m-0">${address.addressDetail}</p>
                    <p class="m=0 address-w-d-p">${address.ward} - ${address.district} - ${address.province}</p>
                </td>
                <td class="text-end">
                    <button type="button" class="btn btn-success text-white btn-sm" onclick="pickAddress(${address.id})">
                        <i class="fas fa-check"></i>
                    </button>
                </td>
            `;
        addressList.appendChild(tr);
        $(document).ready(function() {
            const apiCities = 'https://esgoo.net/api-tinhthanh/1/0.htm';
            const apiDistricts = idProvince => `https://esgoo.net/api-tinhthanh/2/${idProvince}.htm`;
            const apiWards = idDistrict => `https://esgoo.net/api-tinhthanh/3/${idDistrict}.htm`;

            let provincesData = {};
            let districtsData = {};
            let wardsData = {};

            // Function to fetch provinces
            async function fetchProvinces() {
                if (province==""){
                    districtSelect.disabled=true;
                    wardSelect.disabled=true;
                }
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
    });
}

const modalAddAddressEl = document.getElementById("address-modal");
const myModalAddAddressEl = new bootstrap.Modal(modalAddAddressEl, {
    keyboard: false
})
const pickAddress = async (addressId)=>{
    let res = await axios.get(`/api/addresses/${addressId}`);
    toastr.success("Đã chọn địa chỉ thành công!")
    addressDetailInput.value = res.data.addressDetail;
    nameCustomerInput.value = res.data.receiverName;
    phoneInput.value = res.data.phone;
    province = res.data.province;
    district = res.data.district;
    ward = res.data.ward;
    let apiCities = 'https://esgoo.net/api-tinhthanh/1/0.htm';
    let apiDistricts = idProvince => `https://esgoo.net/api-tinhthanh/2/${idProvince}.htm`;
    let apiWards = idDistrict => `https://esgoo.net/api-tinhthanh/3/${idDistrict}.htm`;
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

    // Lấy và hiển thị danh sách các tỉnh/thành phố
    axios.get(apiCities)
        .then(response => {
            const provinceSelect = document.getElementById('province');
            populateOptions(provinceSelect, response.data.data, 'Chọn Tỉnh/Thành Phố');

            // Điền giá trị vào trường tỉnh/thành phố nếu có dữ liệu
            if (province) {
                setSelectedValue(provinceSelect, province);
                $('#province').trigger('change');
            }
        })
        .catch(error => {
            console.error('Lỗi khi lấy danh sách tỉnh/thành phố:', error);
        });

    // Xử lý sự kiện khi chọn tỉnh/thành phố
    $('#province').on('change', function() {
        const provinceCode = this.value;
        if (provinceCode) {
            axios.get(apiDistricts(provinceCode))
                .then(response => {
                    populateOptions(districtSelect, response.data.data, 'Chọn Quận/Huyện');
                    districtSelect.disabled = false;
                    wardSelect.innerHTML = '<option value="">Chọn Phường/Xã</option>';
                    wardSelect.disabled = true;

                    let exists = response.data.data.some(d => d.id == district);

                    // Điền giá trị vào trường quận/huyện nếu có dữ liệu
                    if (exists) {
                        setSelectedValue(districtSelect, district);
                        $('#district').trigger('change');
                    }else {
                        districtSelect.disabled=false;
                        districtSelect.innerHTML='<option value="">Chọn Quận/Huyện</option>';
                        populateOptions(districtSelect, response.data.data, 'Chọn Quận/Huyện');
                    }
                })
                .catch(error => {
                    console.error('Lỗi khi lấy danh sách quận/huyện:', error);
                });
        } else {
            districtSelect.innerHTML = '<option value="">Chọn Quận/Huyện</option>';
            districtSelect.disabled = true;
            wardSelect.innerHTML = '<option value="">Chọn Phường/Xã</option>';
            wardSelect.disabled = true;
        }
        myModalAddAddressEl.hide();
    });

    // Xử lý sự kiện khi chọn quận/huyện
    $('#district').on('change', function() {
        const districtCode = this.value;

        if (districtCode) {
            axios.get(apiWards(districtCode))
                .then(response => {
                    populateOptions(wardSelect, response.data.data, 'Chọn Phường/Xã');
                    wardSelect.disabled = false;

                    let exists = response.data.data.some(w => w.id == ward);

                    // Điền giá trị vào trường phường/xã nếu có dữ liệu
                    if (exists) {
                        setSelectedValue(wardSelect, ward);
                    }else {
                        wardSelect.disabled = false;
                        wardSelect.innerHTML = '<option value="">Chọn Phường/Xã</option>';
                        populateOptions(wardSelect, response.data.data, 'Chọn Phường/Xã');
                    }
                })
                .catch(error => {
                    console.error('Lỗi khi lấy danh sách phường/xã:', error);
                });
        } else {
            wardSelect.innerHTML = '<option value="">Chọn Phường/Xã</option>';
            wardSelect.disabled = true;
        }
    });
}


$(document).ready(function() {
    let apiCities = 'https://esgoo.net/api-tinhthanh/1/0.htm';
    let apiDistricts = idProvince => `https://esgoo.net/api-tinhthanh/2/${idProvince}.htm`;
    let apiWards = idDistrict => `https://esgoo.net/api-tinhthanh/3/${idDistrict}.htm`;


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

    // Lấy và hiển thị danh sách các tỉnh/thành phố
    axios.get(apiCities)
        .then(response => {
            const provinceSelect = document.getElementById('province');
            populateOptions(provinceSelect, response.data.data, 'Chọn Tỉnh/Thành Phố');

            // Điền giá trị vào trường tỉnh/thành phố nếu có dữ liệu
            if (province) {
                setSelectedValue(provinceSelect, province);
                $('#province').trigger('change');
            }
        })
        .catch(error => {
            console.error('Lỗi khi lấy danh sách tỉnh/thành phố:', error);
        });

    // Xử lý sự kiện khi chọn tỉnh/thành phố
    $('#province').on('change', function() {
        const provinceCode = this.value;
        if (provinceCode) {
            axios.get(apiDistricts(provinceCode))
                .then(response => {
                    populateOptions(districtSelect, response.data.data, 'Chọn Quận/Huyện');
                    districtSelect.disabled = false;
                    wardSelect.innerHTML = '<option value="">Chọn Phường/Xã</option>';
                    wardSelect.disabled = true;

                    let exists = response.data.data.some(d => d.id == district);

                    // Điền giá trị vào trường quận/huyện nếu có dữ liệu
                    if (exists) {
                        setSelectedValue(districtSelect, district);
                        $('#district').trigger('change');
                    }else {
                        districtSelect.disabled=false;
                        districtSelect.innerHTML='<option value="">Chọn Quận/Huyện</option>';
                        populateOptions(districtSelect, response.data.data, 'Chọn Quận/Huyện');
                    }
                })
                .catch(error => {
                    console.error('Lỗi khi lấy danh sách quận/huyện:', error);
                });
        } else {
            districtSelect.innerHTML = '<option value="">Chọn Quận/Huyện</option>';
            districtSelect.disabled = true;
            wardSelect.innerHTML = '<option value="">Chọn Phường/Xã</option>';
            wardSelect.disabled = true;
        }
    });

    // Xử lý sự kiện khi chọn quận/huyện
    $('#district').on('change', function() {
        const districtCode = this.value;

        if (districtCode) {
            axios.get(apiWards(districtCode))
                .then(response => {
                    populateOptions(wardSelect, response.data.data, 'Chọn Phường/Xã');
                    wardSelect.disabled = false;

                    let exists = response.data.data.some(w => w.id == ward);

                    // Điền giá trị vào trường phường/xã nếu có dữ liệu
                    if (exists) {
                        setSelectedValue(wardSelect, ward);
                    }else {
                        wardSelect.disabled = false;
                        wardSelect.innerHTML = '<option value="">Chọn Phường/Xã</option>';
                        populateOptions(wardSelect, response.data.data, 'Chọn Phường/Xã');
                    }
                })
                .catch(error => {
                    console.error('Lỗi khi lấy danh sách phường/xã:', error);
                });
        } else {
            wardSelect.innerHTML = '<option value="">Chọn Phường/Xã</option>';
            wardSelect.disabled = true;
        }
    });
});