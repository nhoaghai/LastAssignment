package com.example.demo_clothes_shop_23.service;

import com.example.demo_clothes_shop_23.entities.Address;
import com.example.demo_clothes_shop_23.entities.TokenConfirm;
import com.example.demo_clothes_shop_23.entities.User;
import com.example.demo_clothes_shop_23.exception.BadRequestException;
import com.example.demo_clothes_shop_23.exception.ResourceNotFoundException;
import com.example.demo_clothes_shop_23.model.enums.TokenType;
import com.example.demo_clothes_shop_23.model.enums.UserRole;
import com.example.demo_clothes_shop_23.model.response.VerifyResponse;
import com.example.demo_clothes_shop_23.repository.AddressRepository;
import com.example.demo_clothes_shop_23.repository.TokenConfirmRepository;
import com.example.demo_clothes_shop_23.repository.UserRepository;
import com.example.demo_clothes_shop_23.request.*;
import com.example.demo_clothes_shop_23.security.CustomUserDetails;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final HttpSession httpSession;
    private final AuthenticationManager authenticationManager;
    private final TokenConfirmRepository tokenConfirmRepository;
    private final MailService mailService;

    public void login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken token =
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            httpSession.setAttribute("MY_SESSION", authentication.getName());
        }catch (DisabledException e) {
            throw new BadRequestException("Tài khoản chưa được kích hoạt");
        }catch (AuthenticationException e) {
            throw new BadRequestException("Email hoặc mật khẩu không đúng");
        }
    }

    public void logout() {
        httpSession.setAttribute("user", null);
    }

    public void register(RegisterRequest registerRequest) {
        //Cần kiểm tra user đã tồn tại hay chưa
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()){
            throw new BadRequestException("Email đã sử dụng rồi");
        }
        if (!registerRequest.getConfirmPassword().equals(registerRequest.getPassword())){
            throw new BadRequestException("Mật khẩu xác nhận không trùng khớp");
        }

        //Lưu password vào database cần mã hóa password
        User user = User.builder()
            .name(registerRequest.getName())
            .email(registerRequest.getEmail())
            .password(passwordEncoder.encode(registerRequest.getPassword()))
            .avatar("https://placehold.co/600x600?text=" +String.valueOf(registerRequest.getName().charAt(0)).toUpperCase())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .role(UserRole.USER)
            .enabled(false)
            .build();
        userRepository.save(user);

        Address address = Address.builder()
            .receiverName(registerRequest.getReceiverName())
            .phone(registerRequest.getPhone())
            .province(registerRequest.getProvince())
            .district(registerRequest.getDistrict())
            .ward(registerRequest.getWard())
            .addressDetail(registerRequest.getAddressDetail())
            .chosen(true)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .user(user)
            .build();
        addressRepository.save(address);

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
        String link = "https://dacsanxanh.shop/verifyAccount?token=" + token.getToken();

        //Gửi mail xác thực
        mailService.sendMail2(user, "Xác thực tài khoản", link);
    }

    public User updateInfo(UpdateInfoUserRequest updateInfoUserRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = (User) userDetails.getUser();
        if (!Objects.equals(user.getId(), updateInfoUserRequest.getUserId())){
            throw new ResourceNotFoundException("Không tìm thấy người dùng này");
        }

        user.setName(updateInfoUserRequest.getName());
        userRepository.save(user);
        return user;
    }

    public User updatePassword(UpdatePasswordRequest updatePasswordRequest, Integer id) {
        //Kiểm tra user này có tồn tại hay ko
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = (User) userDetails.getUser();

        if (!Objects.equals(user.getId(), id)){
            throw new ResourceNotFoundException("Không tìm thấy người dùng này");
        }

        if (!passwordEncoder.matches(updatePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("Mật khẩu cũ sai");
        }
        if (!updatePasswordRequest.getNewPassword().equals(updatePasswordRequest.getConfirmPassword())){
            throw new BadRequestException("Mật khẩu mới và mật khẩu khác khác nhau");
        }

        if (updatePasswordRequest.getNewPassword().equals(updatePasswordRequest.getOldPassword())){
            throw new BadRequestException("Mật khẩu mới và mật khẩu cũ giống nhau");
        }
        user.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
        userRepository.save(user);
        return user;
    }

    public VerifyResponse confirmRegistration(String token) {
        Optional<TokenConfirm> tokenConfirmOptional = tokenConfirmRepository
            .findByTokenAndType(token,TokenType.REGISTRATION);

        if (tokenConfirmOptional.isEmpty()){
            return VerifyResponse.builder()
                .success(false)
                .message("Mã xác thực không hợp lệ")
                .build();
        }

        TokenConfirm tokenConfirm = tokenConfirmOptional.get();
        //Token đã được xác thực trc dây
        if (tokenConfirm.getConfirmedAt()!=null){
            return VerifyResponse.builder()
                .success(false)
                .message("Mã xác thực đã được xác thực rồi")
                .build();
        }

        if (tokenConfirm.getExpiresAt().isBefore(LocalDateTime.now())){
            return VerifyResponse.builder()
                .success(false)
                .message("Mã xác thực đã hết hạn ")
                .build();
        }

        User user = tokenConfirm.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        tokenConfirm.setConfirmedAt(LocalDateTime.now());
        tokenConfirmRepository.save(tokenConfirm);

        return VerifyResponse.builder()
            .success(true)
            .message("Xác thực tài khoản thành công!")
            .build();

    }

    public void forgetPassword(ForgetPasswordRequest forgetPasswordRequest) {
        Optional<User> user = userRepository.findByEmail(forgetPasswordRequest.getEmail());
        if (user.isEmpty()){
            throw new BadRequestException("Email chưa được đăng ký");
        }

        //Tạo token xác thực đăng kí
        TokenConfirm token = TokenConfirm.builder()
            .token(UUID.randomUUID().toString())
            .user(user.get())
            .type(TokenType.PASSWORD_RESET)
            .createdAt(LocalDateTime.now())
            .expiresAt(LocalDateTime.now().plusMinutes(30))
            .build();
        tokenConfirmRepository.save(token);

        //Tạo link
        String link = "https://dacsanxanh.shop/changePassword?token=" + token.getToken();

        //Gửi mail
        mailService.sendMail3(user.get(), "Thay đổi mật khẩu", link);
    }

    public VerifyResponse confirmChangePassword(String token) {
        Optional<TokenConfirm> tokenConfirmOptional = tokenConfirmRepository
            .findByTokenAndType(token,TokenType.PASSWORD_RESET);

        if (tokenConfirmOptional.isEmpty()){
            return VerifyResponse.builder()
                .success(false)
                .message("Mã xác thực không hợp lệ")
                .build();
        }

        TokenConfirm tokenConfirm = tokenConfirmOptional.get();
        //Token đã được xác thực trc dây
        if (tokenConfirm.getConfirmedAt()!=null){
            return VerifyResponse.builder()
                .success(false)
                .message("Mã xác thực đã được xác thực rồi")
                .build();
        }

        if (tokenConfirm.getExpiresAt().isBefore(LocalDateTime.now())){
            return VerifyResponse.builder()
                .success(false)
                .message("Mã xác thực đã hết hạn")
                .build();
        }

        return VerifyResponse.builder()
            .success(true)
            .message("Xác thực tài khoản thành công!")
            .build();
    }

    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        Optional<TokenConfirm> tokenConfirmOptional = tokenConfirmRepository
            .findByTokenAndType(changePasswordRequest.getTokenString(),TokenType.PASSWORD_RESET);
        if (tokenConfirmOptional.isEmpty()){
            throw new ResourceNotFoundException("Không tìm thấy token");
        }
        TokenConfirm tokenConfirm = tokenConfirmOptional.get();
        User user = tokenConfirm.getUser();
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getPassword()));
        userRepository.save(user);

        tokenConfirm.setConfirmedAt(LocalDateTime.now());
        tokenConfirmRepository.save(tokenConfirm);
    }
}
