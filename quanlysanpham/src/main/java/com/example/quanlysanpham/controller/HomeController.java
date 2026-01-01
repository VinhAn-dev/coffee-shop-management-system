package com.example.quanlysanpham.controller; // Đã thêm .service để đúng vị trí file

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Xin chào! Tôi là thành viên mới, code đã chạy thành công!";
    }
}