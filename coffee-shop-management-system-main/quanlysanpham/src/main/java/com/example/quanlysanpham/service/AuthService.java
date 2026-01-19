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

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
    Hàm đăng nhập CHUNG cho cả Admin và Staff
     */
    public LoginResponse loginStaff(String username, String password) {
        /*
        kiểm tra đầu vào rỗng 
        phải có vì nếu actor quên nhập thì hiện thông báo
        */ 
        if (username == null || username.isBlank() || password == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vui lòng nhập đầy đủ thông tin");
        }

        // 2. Tìm user trong database
        User user = userRepository.findByUsername(username);

        // 3. KIỂM TRA GỘP (QUAN TRỌNG):
        // Nếu (User không tìm thấy) HOẶC (Mật khẩu không khớp) -> Báo lỗi chung
        if (user == null || !user.getPassword().equals(password)) {
            // Ném lỗi 401 Unauthorized kèm thông báo chung
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sai tài khoản hoặc mật khẩu");
        }

        // 4. Kiểm tra quyền (Chặn nếu không phải ADMIN hoặc STAFF)
        String role = user.getRole();
        if (!"ADMIN".equalsIgnoreCase(role) && !"STAFF".equalsIgnoreCase(role)) {
             throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Tài khoản không có quyền truy cập");
        }

        // 5. Tạo token giả
        String token = "dummy-token-123456"; 

        return new LoginResponse(
                token, 
                user.getRole(), 
                user.getFullName(), 
                user.getId() 
        );
    }

    public void logout(String token) {
        // Xử lý logout sau
    }
}