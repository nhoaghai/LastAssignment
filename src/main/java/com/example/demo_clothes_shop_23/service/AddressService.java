package com.example.demo_clothes_shop_23.service;

import com.example.demo_clothes_shop_23.entities.Address;
import com.example.demo_clothes_shop_23.entities.User;
import com.example.demo_clothes_shop_23.exception.ResourceNotFoundException;
import com.example.demo_clothes_shop_23.repository.AddressRepository;
import com.example.demo_clothes_shop_23.repository.UserRepository;
import com.example.demo_clothes_shop_23.request.UpsertAddressRequest;
import com.example.demo_clothes_shop_23.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public List<Address> getByCurrentUser_Id() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails customUserDetails) {
            User user = (User) customUserDetails.getUser();
            return addressRepository.findByUser_Id(user.getId());
        }else return new ArrayList<>();
    }

    public List<Address> getByUser_Id(Integer userId) {
        return addressRepository.findByUser_Id(userId);
    }

    public Address getByCurrentUser_IdAndChosen() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails customUserDetails) {
            User user = (User) customUserDetails.getUser();
            return addressRepository.findByUser_IdAndChosen(user.getId(), true);
        }else return null;
    }

    public List<Address> createAddress(UpsertAddressRequest addressRequest) {
        User user = userRepository.findById(addressRequest.getUserId()).orElseThrow(
            () -> new  ResourceNotFoundException("Not found user")
        );

        Address address = Address.builder()
            .receiverName(addressRequest.getReceiverName())
            .phone(addressRequest.getPhone())
            .province(addressRequest.getProvince())
            .district(addressRequest.getDistrict())
            .ward(addressRequest.getWard())
            .addressDetail(addressRequest.getAddressDetail())
            .chosen(false)
            .user(user)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        addressRepository.save(address);
        return addressRepository.findByUser_Id(user.getId());
    }

    public List<Address> updateAddress(UpsertAddressRequest addressRequest, Integer id) {

        User user = userRepository.findById(addressRequest.getUserId()).orElseThrow(
            () -> new  ResourceNotFoundException("Not found user")
        );

        Address address = addressRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("No address found with id: " + id)
        );

        if (!addressRepository.findByUser_Id(user.getId()).contains(address) && !Objects.equals(user.getRole().toString(), "ADMIN")) {
            throw new ResourceNotFoundException("Address is not ur own");
        }
        address.setReceiverName(addressRequest.getReceiverName());
        address.setPhone(addressRequest.getPhone());
        address.setProvince(addressRequest.getProvince());
        address.setDistrict(addressRequest.getDistrict());
        address.setWard(addressRequest.getWard());
        address.setAddressDetail(addressRequest.getAddressDetail());
        addressRepository.save(address);
        return addressRepository.findByUser_Id(user.getId());
    }

    public List<Address> deleteAddress( Integer id, Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(
            () -> new  ResourceNotFoundException("Not found user")
        );
        Address address = addressRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("No address found with id: " + id)
        );

        if (!addressRepository.findByUser_Id(user.getId()).contains(address) && !Objects.equals(user.getRole().toString(), "ADMIN")) {
            throw new ResourceNotFoundException("Address is not ur own");
        }
        addressRepository.delete(address);
        return addressRepository.findByUser_Id(user.getId());
    }

    public List<Address> updateChosen(Integer id,Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(
            () -> new  ResourceNotFoundException("Not found user")
        );

        // Tìm địa chỉ cần cập nhật
        Address address = addressRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("No address found with id: " + id)
        );

        // Kiểm tra xem địa chỉ có thuộc về người dùng hiện tại không
        if (!addressRepository.findByUser_Id(user.getId()).contains(address)&& !Objects.equals(user.getRole().toString(), "ADMIN")) {
            throw new ResourceNotFoundException("Address is not your own");
        }

        // Lấy danh sách tất cả địa chỉ của người dùng
        List<Address> addresses = addressRepository.findByUser_Id(user.getId());

        // Cập nhật trạng thái cho tất cả địa chỉ
        addresses.forEach(a -> {
            if (a.getId().equals(id)) {
                a.setChosen(true);  // Đặt địa chỉ được chọn thành true
            } else {
                a.setChosen(false); // Các địa chỉ khác thành false
            }
            addressRepository.save(a);  // Lưu thay đổi cho từng địa chỉ
        });

        // Trả về danh sách địa chỉ đã được cập nhật
        return addresses;
    }

    public Address getAddressById(Integer id) {
        return addressRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("No address found with id: " + id)
        );
    }
}
