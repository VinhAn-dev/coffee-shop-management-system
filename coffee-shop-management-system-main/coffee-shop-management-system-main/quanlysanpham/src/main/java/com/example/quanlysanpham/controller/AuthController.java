package com.example.quanlysanpham.controller;

import com.example.quanlysanpham.dto.LoginRequest;
import com.example.quanlysanpham.dto.LoginResponse;
import com.example.quanlysanpham.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/staff/login")
    public ResponseEntity<LoginResponse> staffLogin(@RequestBody LoginRequest req) {
        // Gọi sang Service để kiểm tra mật khẩu
        LoginResponse response = authService.loginStaff(req.getUsername(), req.getPassword());
        return ResponseEntity.ok(response);
    }
}