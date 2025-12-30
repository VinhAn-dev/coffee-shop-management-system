package com.example.quanlysanpham.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.quanlysanpham.dto.LoginRequest;
import com.example.quanlysanpham.dto.LoginResponse;
import com.example.quanlysanpham.service.AuthService;

/**
 * API xác thực (auth).
 * Nhánh STAFF dùng endpoint /api/auth/staff/login
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * STAFF login.
     * Client gửi JSON { "username": "...", "password": "..." }
     * Thành công trả về token.
     */
    @PostMapping("/staff/login")
    public ResponseEntity<LoginResponse> staffLogin(@RequestBody LoginRequest req) {
        LoginResponse res = authService.loginStaff(req.getUsername(), req.getPassword());
        return ResponseEntity.ok(res);
    }

    /**
     * Logout: client gửi header Authorization: Bearer <token>
     * Server xóa token khỏi TokenStore.
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader(value = "Authorization", required = false) String authorization) {
        String token = extractBearerToken(authorization);
        authService.logout(token);
        return ResponseEntity.ok("Logout OK");
    }

    /**
     * Tách token từ header "Authorization: Bearer <token>"
     * - Nếu không có header hoặc sai format -> trả null
     */
    private String extractBearerToken(String authorization) {
        if (authorization == null) return null;

        String prefix = "Bearer ";
        if (!authorization.startsWith(prefix)) return null;

        return authorization.substring(prefix.length()).trim();
    }
}
