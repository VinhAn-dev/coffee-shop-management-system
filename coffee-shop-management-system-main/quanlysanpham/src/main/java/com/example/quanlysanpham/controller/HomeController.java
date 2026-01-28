package com.example.quanlysanpham.controller; // Đã thêm .service để đúng vị trí file

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller  // Lưu ý: Dùng @Controller, KHÔNG dùng @RestController
public class HomeController {

    // Khi người dùng vào trang chủ "/" (localhost:8080)
    @GetMapping("/")
    public String home() {
        // Nó sẽ tự chuyển hướng sang "index.html"
        return "redirect:/index.html";
    }
}