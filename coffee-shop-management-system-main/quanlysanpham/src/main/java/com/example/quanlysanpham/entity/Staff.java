package com.example.quanlysanpham.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "staffs")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(unique = true) // Username không được trùng nhau
    private String username;

    private String password;

    // --- 🔥 QUAN TRỌNG: ĐÃ THÊM CỘT ROLE ĐỂ SỬA LỖI 🔥 ---
    private String role = "STAFF"; // Mặc định là nhân viên

    public Staff() {
    }

    public Staff(String fullName, String username, String password) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
    }

    // ===== Getter và Setter (Đã thêm setRole/getRole) =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    // 👇 Đây là 2 hàm quan trọng giúp sửa lỗi "The method setRole is undefined"
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}