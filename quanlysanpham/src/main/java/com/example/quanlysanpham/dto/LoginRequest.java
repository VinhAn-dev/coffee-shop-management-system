package com.example.quanlysanpham.dto;

/**
 * Dữ liệu client gửi lên khi đăng nhập.
 */
public class LoginRequest {

    // tên đăng nhập (dùng để tìm User trong DB)
    private String username;

    // mật khẩu (MVP: đang lưu dạng String, chưa hash)
    private String password;

    public LoginRequest() {}

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // ===== Getter/Setter =====
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
