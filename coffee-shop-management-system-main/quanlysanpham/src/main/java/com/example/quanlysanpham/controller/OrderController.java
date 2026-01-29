package com.example.quanlysanpham.controller;

import com.example.quanlysanpham.entity.Order;
import com.example.quanlysanpham.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*") // Cho phép Frontend gọi API thoải mái
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 1. LẤY DANH SÁCH TẤT CẢ ĐƠN (Dùng cho trang Lịch sử Admin)
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    // 2. LẤY CHI TIẾT 1 ĐƠN (Nếu cần xem kỹ đơn đó có món gì)
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        if (order != null) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
    }
    
    // Lưu ý: Hàm Tạo đơn (createOrder) đã được chuyển sang POSController (/api/checkout)
    // nên không cần viết ở đây nữa để tránh xung đột code.
}