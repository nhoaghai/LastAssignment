package com.example.demo_clothes_shop_23.rest.adminApi;

import com.example.demo_clothes_shop_23.request.CreateUserRequest;
import com.example.demo_clothes_shop_23.request.UpdateUserRequest;
import com.example.demo_clothes_shop_23.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@AllArgsConstructor
public class AdminUserApi {
    private final UserService userService;

    @PutMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest createUserRequest) {
        userService.createUser(createUserRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest updateUserRequest, @PathVariable Integer id) {
        userService.updateUser(updateUserRequest,id);
        return ResponseEntity.ok().build();
    }
}
