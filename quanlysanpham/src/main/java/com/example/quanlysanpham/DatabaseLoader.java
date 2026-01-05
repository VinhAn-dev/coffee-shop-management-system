package com.example.quanlysanpham;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.quanlysanpham.entity.Product;
import com.example.quanlysanpham.entity.User;
import com.example.quanlysanpham.repository.ProductRepository;
import com.example.quanlysanpham.repository.UserRepository;

@Configuration
public class DatabaseLoader {

    // Chạy ngay khi Server khởi động
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepo, ProductRepository productRepo) {
        return args -> {
            // 1. Tạo User ADMIN nếu chưa có
            if (userRepo.findByUsername("admin") == null) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword("123"); // Lưu ý: thực tế nên mã hóa
                admin.setRole("ADMIN");
                admin.setFullName("Quản Trị Viên");
                userRepo.save(admin);
                System.out.println(">>> Đã tạo user: admin / 123");
            }

            // 2. Tạo User STAFF nếu chưa có
            if (userRepo.findByUsername("staff") == null) {
                User staff = new User();
                staff.setUsername("staff");
                staff.setPassword("123");
                staff.setRole("STAFF");
                staff.setFullName("Nhân Viên A");
                userRepo.save(staff);
                System.out.println(">>> Đã tạo user: staff / 123");
            }

            // 3. Tạo vài món mẫu nếu menu trống
            if (productRepo.count() == 0) {
                Product p1 = new Product();
                p1.setName("Cà phê Đen");
                p1.setPrice(15000.0);
                p1.setIsAvailable(true);

                Product p2 = new Product();
                p2.setName("Cà phê Sữa");
                p2.setPrice(18000.0);
                p2.setIsAvailable(true);

                productRepo.saveAll(List.of(p1, p2));
                System.out.println(">>> Đã tạo dữ liệu menu mẫu");
            }
        };
    }
}