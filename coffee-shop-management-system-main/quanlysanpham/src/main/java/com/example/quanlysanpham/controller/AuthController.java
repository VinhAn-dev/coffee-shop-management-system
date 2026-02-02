package com.example.quanlysanpham.controller;

import com.example.quanlysanpham.dto.LoginRequest;
import com.example.quanlysanpham.dto.LoginResponse;
import com.example.quanlysanpham.entity.Staff; // Import Staff
import com.example.quanlysanpham.repository.StaffRepository; // Import Repository
import com.example.quanlysanpham.service.AuthService;

import jakarta.servlet.http.HttpSession; // 👇 1. Import Session

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // 👇 2. Cần thêm Repository để tìm thông tin đầy đủ của nhân viên
    @Autowired
    private StaffRepository staffRepository;

    @PostMapping("/staff/login")
    // 👇 3. Thêm tham số HttpSession session vào hàm này
    public ResponseEntity<LoginResponse> staffLogin(@RequestBody LoginRequest req, HttpSession session) {
        
        // Gọi Service check mật khẩu (Nếu sai pass thường Service sẽ ném lỗi, code dừng tại đây)
        LoginResponse response = authService.loginStaff(req.getUsername(), req.getPassword());

        // 👇 4. QUAN TRỌNG: Lưu nhân viên vào Session
        // (Để POSController bên kia có thể móc ra dùng)
        Staff staff = staffRepository.findByUsername(req.getUsername());
        
        if (staff != null) {
            session.setAttribute("loggedInUser", staff); // Key "loggedInUser" phải khớp với bên POS
            System.out.println("DEBUG: Đã lưu Session cho nhân viên: " + staff.getFullName());
        }

        return ResponseEntity.ok(response);
    }
}