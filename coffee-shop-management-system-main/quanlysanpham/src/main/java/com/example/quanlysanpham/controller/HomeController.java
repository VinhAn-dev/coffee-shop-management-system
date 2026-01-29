package com.example.quanlysanpham.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute; // Import Product
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.quanlysanpham.entity.Product;
import com.example.quanlysanpham.repository.OrderRepository;
import com.example.quanlysanpham.repository.ProductRepository;
import com.example.quanlysanpham.repository.StaffRepository;
import com.example.quanlysanpham.service.AuthService;

@Controller
public class HomeController {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private AuthService authService;
    
    @Autowired
    private ProductRepository productRepo;
    
    @Autowired
    private OrderRepository orderRepo;

    // --- 1. TRANG CHỦ & ĐIỀU HƯỚNG ---
    @GetMapping("/")
    public String home() {
        return "redirect:/index.html"; 
    }

    // --- 2. QUẢN LÝ THỰC ĐƠN (READ) ---
    @GetMapping("/products")
    public String showProductPage(Model model) {
        model.addAttribute("products", productRepo.findAll());
        return "product-list"; 
    }

    // --- 👇 PHẦN MỚI THÊM: CRUD SẢN PHẨM 👇 ---

    // Mở form thêm mới
    @GetMapping("/products/new")
    public String showNewProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("pageTitle", "Thêm Món Mới");
        return "product-form"; // Cần tạo file product-form.html
    }

    // Lưu sản phẩm (Dùng cho cả Thêm mới và Sửa)
    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute("product") Product product) {
        productRepo.save(product);
        return "redirect:/products";
    }

    // Mở form sửa
    @GetMapping("/products/edit/{id}")
    public String showEditProductForm(@PathVariable("id") Long id, Model model) {
        Product product = productRepo.findById(id).orElse(null);
        if (product != null) {
            model.addAttribute("product", product);
            model.addAttribute("pageTitle", "Sửa Món Ăn (ID: " + id + ")");
            return "product-form"; // Tái sử dụng form
        }
        return "redirect:/products";
    }

    // Xóa sản phẩm
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productRepo.deleteById(id);
        return "redirect:/products";
    }
    // ---------------------------------------------

    // --- 3. LỊCH SỬ ĐƠN HÀNG ---
    @GetMapping("/admin/history")
    public String showHistoryPage(Model model) {
        model.addAttribute("orders", orderRepo.findAll());
        return "order-history";
    }

    // --- 4. QUẢN LÝ NHÂN VIÊN ---
    @GetMapping("/admin/staff")
    public String showStaffManagement(Model model) {
        model.addAttribute("staffList", staffRepository.findAll());
        return "staff"; 
    }

    @PostMapping("/admin/staff/add")
    public String addStaff(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String fullName,
                           Model model) {
        try {
            authService.createStaff(username, password, fullName);
            return "redirect:/admin/staff"; 
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("staffList", staffRepository.findAll());
            return "staff";
        }
    }

    @GetMapping("/admin/staff/delete/{id}")
    public String deleteStaff(@PathVariable Long id) {
        try {
            staffRepository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/admin/staff";
    }
}