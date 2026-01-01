package com.example.quanlysanpham; // Nhớ kiểm tra package cho đúng với máy bạn

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.quanlysanpham.entity.Product;
import com.example.quanlysanpham.repository.ProductRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        // Kiểm tra xem nếu chưa có dữ liệu thì mới thêm vào
        if (productRepository.count() == 0) {
            
            // Tạo sản phẩm mẫu 1
            Product p1 = new Product();
            p1.setName("Cà phê Đen");
            p1.setPrice(new BigDecimal("25000")); // Dùng BigDecimal cho giá tiền
            p1.setDescription("Đậm đà hương vị truyền thống");
            p1.setImageUrl("https://via.placeholder.com/150");
            productRepository.save(p1);

            // Tạo sản phẩm mẫu 2
            Product p2 = new Product();
            p2.setName("Bạc Xỉu");
            p2.setPrice(new BigDecimal("28000"));
            p2.setDescription("Ngọt ngào sữa đặc");
            p2.setImageUrl("https://via.placeholder.com/150");
            productRepository.save(p2);

            System.out.println("--- Đã nạp dữ liệu mẫu thành công! ---");
        }
    }
}