package com.example.quanlysanpham.dto;

public class LoginResponse {
    private String token;
    private String role;
    private String fullName;

    public LoginResponse(String token, String role, String fullName) {
        this.token = token;
        this.role = role;
        this.fullName = fullName;
    }

    // Getter và Setter...
    public String getToken() { return token; }
    public String getRole() { return role; }
    public String getFullName() { return fullName; }
}