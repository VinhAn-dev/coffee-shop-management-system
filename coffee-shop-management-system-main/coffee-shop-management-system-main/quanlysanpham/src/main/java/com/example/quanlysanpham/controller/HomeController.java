package com.example.quanlysanpham.controller;

import com.example.quanlysanpham.repository.StaffRepository;
import com.example.quanlysanpham.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private AuthService authService;

    // --- 👇 SỬA LẠI ĐÚNG Ở ĐÂY 👇 ---
    // Khi bấm "Về trang chủ" hoặc vào localhost:8080
    @GetMapping("/")
    public String home() {
        // Thay vì vào /pos (bán hàng), hãy về Bảng Điều Khiển
        return "redirect:/index.html"; 
    }
    // --------------------------------

    // 1. HIỂN THỊ TRANG QUẢN LÝ NHÂN VIÊN
    @GetMapping("/admin/staff")
    public String showStaffManagement(Model model) {
        // Lấy danh sách nhân viên để hiện lên bảng
        model.addAttribute("staffList", staffRepository.findAll());
        return "staff-management"; 
    }

    // 2. XỬ LÝ: THÊM NHÂN VIÊN
    @PostMapping("/admin/staff/add")
    public String addStaff(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String fullName,
                           Model model) {
        try {
            // Gọi hàm tạo nhân viên bên AuthService
            authService.createStaff(username, password, fullName);
            
            // Thành công: Load lại trang danh sách
            return "redirect:/admin/staff"; 
        } catch (Exception e) {
            // Thất bại: Hiện lỗi ra màn hình
            model.addAttribute("error", e.getMessage());
            model.addAttribute("staffList", staffRepository.findAll());
            return "staff-management";
        }
    }

    // 3. XỬ LÝ: XÓA NHÂN VIÊN
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