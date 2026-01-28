package com.example.quanlysanpham.controller;

import com.example.quanlysanpham.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.data.domain.Sort;

@Controller
public class OrderHistoryController {

    @Autowired
    private OrderRepository orderRepository;

    // Đường dẫn để xem lịch sử: localhost:8080/admin/history
    @GetMapping("/admin/history")
    public String showHistory(Model model) {
        // Lấy danh sách đơn, sắp xếp đơn mới nhất lên đầu (DESC)
        model.addAttribute("orders", orderRepository.findAll(Sort.by(Sort.Direction.DESC, "id")));
        
        return "order-history"; // Trả về file HTML vừa tạo
    }
}