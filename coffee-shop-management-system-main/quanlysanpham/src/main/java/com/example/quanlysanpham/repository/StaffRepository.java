package com.example.quanlysanpham.repository;

import com.example.quanlysanpham.entity.User;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<User, Long> {
    // --- THÊM DÒNG NÀY ĐỂ SỬA LỖI ---
    User findByUsername(String username);
    List<User> findByRole(String role);
}