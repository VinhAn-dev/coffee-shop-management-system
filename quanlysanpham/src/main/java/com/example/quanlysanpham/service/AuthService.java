package com.example.quanlysanpham.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.quanlysanpham.auth.TokenStore;
import com.example.quanlysanpham.dto.LoginResponse;
import com.example.quanlysanpham.entity.User;
import com.example.quanlysanpham.repository.UserRepository;

/**
 * Xử lý nghiệp vụ đăng nhập/đăng xuất.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final TokenStore tokenStore;

    public AuthService(UserRepository userRepository, TokenStore tokenStore) {
        this.userRepository = userRepository;
        this.tokenStore = tokenStore;
    }

    /**
     * Đăng nhập riêng cho STAFF.
     * Điều kiện hợp lệ:
     * - username tồn tại
     * - password khớp
     * - role = "STAFF"
     * Thành công -> cấp token và trả về LoginResponse.
     */
    public LoginResponse loginStaff(String username, String password) {
        if (username == null || username.isBlank() || password == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Thiếu username/password");
        }

        Optional<User> opt = userRepository.findByUsername(username);
        if (opt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sai tài khoản hoặc mật khẩu");
        }

        User user = opt.get();

        // So sánh password (MVP: plaintext)
        if (!password.equals(user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sai tài khoản hoặc mật khẩu");
        }

        // Chỉ cho STAFF đăng nhập ở nhánh staff
        if (user.getRole() == null || !user.getRole().equalsIgnoreCase("STAFF")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Tài khoản không phải STAFF");
        }

        String token = tokenStore.issueToken(user.getId());

        return new LoginResponse(
                token,
                user.getRole(),
                user.getFullName(),
                user.getId()
        );
    }

    /**
     * Đăng xuất: hủy token hiện tại.
     */
    public void logout(String token) {
        tokenStore.invalidate(token);
    }
}
