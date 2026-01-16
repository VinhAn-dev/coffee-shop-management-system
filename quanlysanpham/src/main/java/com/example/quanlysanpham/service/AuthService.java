package com.example.quanlysanpham.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.quanlysanpham.dto.LoginResponse;
import com.example.quanlysanpham.entity.User;
import com.example.quanlysanpham.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    // private final TokenStore tokenStore; // Tạm tắt

    // Constructor bỏ TokenStore tạm thời để test cho dễ
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Hàm đăng nhập CHUNG cho cả Admin và Staff
     */
    public LoginResponse loginStaff(String username, String password) {
        // 1. Kiểm tra đầu vào
        if (username == null || username.isBlank() || password == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Thiếu username/password");
        }

        // 2. Tìm user (SỬA LỖI Ở ĐÂY: User không phải là Optional)
        User user = userRepository.findByUsername(username);

        // Nếu không tìm thấy thì user sẽ là null
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Tài khoản không tồn tại");
        }

        // 3. So sánh password
        if (!user.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sai mật khẩu");
        }

        // 4. [QUAN TRỌNG] Bỏ đoạn chặn Admin đi. 
        // Hoặc cho phép nếu là ADMIN hoặc STAFF
        String role = user.getRole();
        if (!"ADMIN".equalsIgnoreCase(role) && !"STAFF".equalsIgnoreCase(role)) {
             throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Tài khoản không có quyền truy cập");
        }

        // 5. Tạo token giả (hoặc dùng TokenStore của mày nếu đã có)
        // String token = tokenStore.issueToken(user.getId()); 
        String token = "dummy-token-123456"; // Token giả để test trước

        return new LoginResponse(
                token, // Trả về token
                user.getRole(), // role
                user.getFullName(), // fullName
                user.getId() // id
        );
    }

    public void logout(String token) {
        // tokenStore.invalidate(token);
    }
}