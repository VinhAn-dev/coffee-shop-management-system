package com.example.quanlysanpham.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.quanlysanpham.entity.User;

// tìm kiếm và lưu user
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // dùng cho login
    Optional<User> findByUsername(String username);

    // (tùy chọn) check username đã tồn tại chưa (đăng ký/tạo user)
    boolean existsByUsername(String username);
}
