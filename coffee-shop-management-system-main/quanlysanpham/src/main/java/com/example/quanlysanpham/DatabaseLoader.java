package com.example.quanlysanpham;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.quanlysanpham.entity.User;
import com.example.quanlysanpham.repository.UserRepository;

@Configuration
public class DatabaseLoader {

    @Bean
    // Bỏ productRepo khỏi tham số vì không dùng nữa
    CommandLineRunner initDatabase(UserRepository userRepo) { 
        return args -> {
            // 1. Giữ lại tạo User ADMIN (Để đăng nhập)
            if (userRepo.findByUsername("admin") == null) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword("123");
                admin.setRole("ADMIN");
                admin.setFullName("Quản Trị Viên");
                userRepo.save(admin);
                System.out.println(">>> Đã tạo user: admin / 123");
            }
        };
    }
}