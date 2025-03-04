package com.example.demo_clothes_shop_23.rest;

import com.example.demo_clothes_shop_23.entities.User;
import com.example.demo_clothes_shop_23.request.*;
import com.example.demo_clothes_shop_23.service.AuthService;
import com.example.demo_clothes_shop_23.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApi {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
        return ResponseEntity.ok("Đăng ký thành công!");//201
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        authService.login(loginRequest);
        return ResponseEntity.ok("Đăng nhập thành công!");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        authService.logout();
        return ResponseEntity.ok("Đăng xuất thành công!");
    }

    @PutMapping("/updateInfo")
    public ResponseEntity<?> updateInfo(@Valid @RequestBody UpdateInfoUserRequest updateInfoUserRequest){
        User user= authService.updateInfo(updateInfoUserRequest);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/update-password/{id}")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest, @Valid @PathVariable Integer id) {
        User user = authService.updatePassword(updatePasswordRequest,id);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/forgetPassword")
    public ResponseEntity<?> forgetPassword(@Valid @RequestBody ForgetPasswordRequest forgetPasswordRequest) {
        authService.forgetPassword(forgetPasswordRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        authService.changePassword(changePasswordRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/update-avatar")
    public ResponseEntity<?> updateAvatar(@RequestParam("file") MultipartFile file, @PathVariable Integer id) {
        return ResponseEntity.ok(userService.updateAvatar(id, file));
    }
}
