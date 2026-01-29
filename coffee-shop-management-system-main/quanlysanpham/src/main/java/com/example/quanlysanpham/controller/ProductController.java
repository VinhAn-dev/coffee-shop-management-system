package com.example.quanlysanpham.controller;

import com.example.quanlysanpham.entity.Product;
import com.example.quanlysanpham.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller; // Dùng Controller này mới ra giao diện
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller // Đổi từ @RestController sang @Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // 1. Hiển thị danh sách (GET /products)
    // Đường dẫn này khớp với menu "Quản lý thực đơn" ở trang chủ
    @GetMapping("/products")
    public String listProducts(Model model) {
        // Lấy danh sách từ DB gửi sang HTML
        model.addAttribute("products", productRepository.findAll());
        return "product-list"; // Trả về file product-list.html
    }

    // 2. Hiển thị form thêm mới (GET /products/new)
    @GetMapping("/products/new")
    public String showNewForm(Model model) {
        model.addAttribute("product", new Product());
        return "product-form"; // Trả về file product-form.html
    }

    // 3. Xử lý lưu sản phẩm (POST /products/save)
    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute("product") Product product) {
        productRepository.save(product);
        return "redirect:/products"; // Lưu xong quay về trang danh sách
    }

    // 4. Hiển thị form sửa (GET /products/edit/{id})
    @GetMapping("/products/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        return "product-form"; // Dùng lại form thêm mới để làm form sửa
    }

    // 5. Xóa sản phẩm (GET /products/delete/{id})
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
        return "redirect:/products"; // Xóa xong quay về trang danh sách
    }
}