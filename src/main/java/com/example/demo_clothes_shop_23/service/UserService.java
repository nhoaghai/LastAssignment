package com.example.demo_clothes_shop_23.service;

import com.example.demo_clothes_shop_23.entities.Address;
import com.example.demo_clothes_shop_23.entities.TokenConfirm;
import com.example.demo_clothes_shop_23.entities.User;
import com.example.demo_clothes_shop_23.exception.BadRequestException;
import com.example.demo_clothes_shop_23.exception.ResourceNotFoundException;
import com.example.demo_clothes_shop_23.model.enums.TokenType;
import com.example.demo_clothes_shop_23.model.enums.UserRole;
import com.example.demo_clothes_shop_23.model.response.ImageResponse;
import com.example.demo_clothes_shop_23.repository.TokenConfirmRepository;
import com.example.demo_clothes_shop_23.repository.UserRepository;
import com.example.demo_clothes_shop_23.request.CreateUserRequest;
import com.example.demo_clothes_shop_23.request.UpdateUserRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenConfirmRepository tokenConfirmRepository;
    private final MailService mailService;
    private final FileServerService fileServerService;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(int id) {
        return userRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("User not found")
        );
    }

    public List<User> getUsersCreatedThisMonth() {
        LocalDateTime now = LocalDateTime.now();
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();
        return userRepository.findUsersCreatedThisMonth(currentMonth, currentYear);
    }

    public List<User> getByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    public void createUser(CreateUserRequest createUserRequest) {
        //Cần kiểm tra user đã tồn tại hay chưa
        if (userRepository.findByEmail(createUserRequest.getEmail()).isPresent()){
            throw new BadRequestException("Email đã sử dụng rồi");
        }

        //Lưu password vào database cần mã hóa password
        User user = User.builder()
            .name(createUserRequest.getName())
            .email(createUserRequest.getEmail())
            .password(passwordEncoder.encode(createUserRequest.getPassword()))
            .avatar("https://placehold.co/600x600?text=" +String.valueOf(createUserRequest.getName().charAt(0)).toUpperCase())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .role(UserRole.valueOf(createUserRequest.getRoleString()))
            .enabled(false)
            .build();
        userRepository.save(user);

        //Tạo token xác thực đăng kí
        TokenConfirm token = TokenConfirm.builder()
            .token(UUID.randomUUID().toString())
            .user(user)
            .type(TokenType.REGISTRATION)
            .createdAt(LocalDateTime.now())
            .expiresAt(LocalDateTime.now().plusMinutes(30))
            .build();
        tokenConfirmRepository.save(token);

        //Tạo link xác thực đăng kí
        String link = "http://localhost:8080/verifyAccount?token=" + token.getToken();

        //Gửi mail xác thực
        mailService.sendMail2(user, "Xác thực tài khoản", link);
    }

    public void updateUser(UpdateUserRequest updateUserRequest, Integer id) {
        User user = userRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Không thấy user này")
        );
        user.setName(updateUserRequest.getName());
        user.setRole(UserRole.valueOf(updateUserRequest.getRoleString()));
        user.setEnabled(updateUserRequest.getEnabled());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public String updateAvatar(Integer userId, MultipartFile file) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));

        ImageResponse imageResponse = fileServerService.uploadFile(file);
        user.setAvatar(imageResponse.getUrl());
        userRepository.save(user);
        return user.getAvatar();
    }
}
