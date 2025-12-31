package com.example.quanlysanpham.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.quanlysanpham.entity.Product;
import com.example.quanlysanpham.repository.ProductRepository;

/**
 * Staff xem menu (chỉ lấy món đang bán).
 * URL prefix: /api/staff
 */
@RestController
@RequestMapping("/api/staff")
public class StaffProductController {

    // productRepository: dùng để truy vấn bảng products (JPA)
    private final ProductRepository productRepository;

    // Inject ProductRepository để controller gọi DB
    public StaffProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * View menu cho staff.
     * - Trả về danh sách món còn bán (isAvailable = true)
     * - ResponseEntity: bọc response + status code
     */
    @GetMapping("/products")
    public ResponseEntity<List<Product>> viewMenu() {

        // menu: danh sách món đang available để hiển thị cho nhân viên chọn
        List<Product> menu = productRepository.findByIsAvailableTrue();

        return ResponseEntity.ok(menu);
    }
}
