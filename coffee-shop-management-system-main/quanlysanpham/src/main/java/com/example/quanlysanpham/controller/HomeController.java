package com.example.quanlysanpham.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.quanlysanpham.entity.Order;
import com.example.quanlysanpham.entity.Product;
import com.example.quanlysanpham.repository.ProductRepository;
import com.example.quanlysanpham.repository.StaffRepository;
import com.example.quanlysanpham.service.AuthService;
import com.example.quanlysanpham.service.OrderService;

@Controller
public class HomeController {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private AuthService authService;
    
    @Autowired
    private ProductRepository productRepo;
        
    @Autowired
    private OrderService orderService; // Chúng ta dùng Service để xử lý logic

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

    // --- CRUD SẢN PHẨM ---
    @GetMapping("/products/new")
    public String showNewProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("pageTitle", "Thêm Món Mới");
        return "product-form";
    }

    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute("product") Product product) {
        productRepo.save(product);
        return "redirect:/products";
    }

    @GetMapping("/products/edit/{id}")
    public String showEditProductForm(@PathVariable("id") Long id, Model model) {
        Product product = productRepo.findById(id).orElse(null);
        if (product != null) {
            model.addAttribute("product", product);
            model.addAttribute("pageTitle", "Sửa Món Ăn (ID: " + id + ")");
            return "product-form";
        }
        return "redirect:/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productRepo.deleteById(id);
        return "redirect:/products";
    }

    // --- 3. LỊCH SỬ ĐƠN HÀNG VỚI BỘ LỌC & PHÂN TRANG ---
    @GetMapping("/admin/history")
    public String showHistoryPage(
            @RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(name = "staffName", defaultValue = "") String staffName,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {
        
        // Mặc định hiển thị 10 đơn hàng mỗi trang để tối ưu hiệu năng
        int pageSize = 10; 
        
        // Gọi xuống OrderService để lấy dữ liệu đã được lọc và cắt nhỏ (Pagination)
        Page<Order> orderPage = orderService.getOrdersWithFilter(date, staffName, PageRequest.of(page, pageSize));
        
        // Đẩy dữ liệu ra view (order-history.html)
        model.addAttribute("orders", orderPage.getContent());     // Danh sách đơn của trang hiện tại
        model.addAttribute("currentPage", page);                  // Trang hiện tại (0, 1, 2...)
        model.addAttribute("totalPages", orderPage.getTotalPages()); // Tổng số trang
        
        // Trả lại giá trị lọc để giữ nguyên trên ô input sau khi bấm tìm kiếm
        model.addAttribute("selectedDate", date);
        model.addAttribute("staffName", staffName);
        
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