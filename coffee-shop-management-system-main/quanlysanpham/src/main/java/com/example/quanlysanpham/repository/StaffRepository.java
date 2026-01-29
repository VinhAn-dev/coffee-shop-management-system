package com.example.quanlysanpham.repository;

import com.example.quanlysanpham.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    // --- THÊM DÒNG NÀY ĐỂ SỬA LỖI ---
    Staff findByUsername(String username);
}