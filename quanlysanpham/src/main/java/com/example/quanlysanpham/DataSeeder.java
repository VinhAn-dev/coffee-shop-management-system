package com.example.quanlysanpham;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.quanlysanpham.entity.Product;
import com.example.quanlysanpham.entity.User;
import com.example.quanlysanpham.repository.ProductRepository;
import com.example.quanlysanpham.repository.UserRepository;

@Component
@Profile("dev") // chỉ seed khi chạy môi trường dev (không seed ở prod)
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public DataSeeder(UserRepository userRepository,
                      ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {

        // ===== Seed user hệ thống =====
        upsertUser("admin", "123", "ADMIN", "Admin Demo");
        upsertUser("staff1", "123", "STAFF", "Staff Demo");

        // ===== Seed menu (product) =====
        upsertProduct("Ca phe sua", 25000.0, true);
        upsertProduct("Latte", 30000.0, true);
    }

    private User upsertUser(String username, String password, String role, String fullName) {
        return userRepository.findByUsername(username)
                .map(existing -> {
                    // nếu đã có thì cập nhật lại cho đúng cấu hình seed
                    existing.setPassword(password);
                    existing.setRole(role);
                    existing.setFullName(fullName);
                    return userRepository.save(existing);
                })
                .orElseGet(() -> userRepository.save(new User(username, password, role, fullName)));
    }

    private Product upsertProduct(String name, Double price, Boolean isAvailable) {
        // repo của bạn đang có findByNameContainingIgnoreCase, nên seeder check theo equalsIgnoreCase
        List<Product> candidates = productRepository.findByNameContainingIgnoreCase(name);

        for (Product p : candidates) {
            if (p.getName() != null && p.getName().equalsIgnoreCase(name)) {
                p.setPrice(price);
                p.setIsAvailable(isAvailable);
                return productRepository.save(p);
            }
        }

        return productRepository.save(new Product(name, price, isAvailable));
    }
}
