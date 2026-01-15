package com.example.quanlysanpham;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.quanlysanpham.entity.User;
import com.example.quanlysanpham.repository.UserRepository;
// import com.example.quanlysanpham.repository.ProductRepository; // Không dùng nữa thì bỏ import luôn cho sạch

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

            // 2. Giữ lại tạo User STAFF
            if (userRepo.findByUsername("staff") == null) {
                User staff = new User();
                staff.setUsername("staff");
                staff.setPassword("123");
                staff.setRole("STAFF");
                staff.setFullName("Nhân Viên Bán Hàng");
                userRepo.save(staff);
                System.out.println(">>> Đã tạo user: staff / 123");
            }
        };
    }
}