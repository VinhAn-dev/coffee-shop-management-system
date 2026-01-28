package com.example.quanlysanpham.dto;

/**
 * Dữ liệu trả về sau khi đăng nhập thành công.
 */
public class LoginResponse {

    // token phiên đăng nhập (client sẽ lưu và gửi lại trong header Authorization)
    private String token;

    // role của tài khoản (STAFF/ADMIN)
    private String role;

    // tên hiển thị
    private String fullName;

    // id user để tiện debug / hiển thị
    private Long userId;

    public LoginResponse() {}

    public LoginResponse(String token, String role, String fullName, Long userId) {
        this.token = token;
        this.role = role;
        this.fullName = fullName;
        this.userId = userId;
    }

    // ===== Getter/Setter =====
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
