package com.example.demo_clothes_shop_23.rest.adminApi;

import com.example.demo_clothes_shop_23.entities.Address;
import com.example.demo_clothes_shop_23.service.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/addresses")
@AllArgsConstructor
public class AdminAddressApi {
    private final AddressService addressService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAddressByUserId(@PathVariable("userId") int userId) {
        List<Address> addresses = addressService.getByUser_Id(userId);
        return ResponseEntity.ok(addresses);
    }
}
