package com.example.quanlysanpham.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.quanlysanpham.dto.OrderRequest;
import com.example.quanlysanpham.entity.Order;
import com.example.quanlysanpham.service.OrderService;

@RestController
@RequestMapping("/api/orders") // Định nghĩa: Mọi link API trong này đều bắt đầu bằng /api/orders
@CrossOrigin(origins = "*")    // Cho phép Frontend kết nối vào mà không bị chặn
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Hàm này dùng để: TẠO ĐƠN HÀNG MỚI
    // (Được gọi khi khách bấm nút "Thanh toán" ở Frontend)
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest request) {
        try {
            Order newOrder = orderService.createOrder(request);
            return ResponseEntity.ok(newOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2. CẬP NHẬT ĐƠN (Khi đang ở chế độ sửa đơn -> bấm "Thanh Toán" hoặc "Lưu lại")
    // URL: PUT http://localhost:8080/api/orders/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody OrderRequest request) {
        Order updatedOrder = orderService.updateOrder(id, request);
        
        if (updatedOrder != null) {
            return ResponseEntity.ok(updatedOrder);
        } else {
            return ResponseEntity.badRequest().body("Không tìm thấy đơn hàng hoặc đơn đã chốt!");
        }
    }

    // 3. LẤY CHI TIẾT 1 ĐƠN (Để hiển thị lại lên màn hình khi bấm sửa)
    // URL: GET http://localhost:8080/api/orders/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        if (order != null) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
    }

    // 4. LẤY DANH SÁCH TẤT CẢ ĐƠN (Để xem lịch sử bán hàng)
    // URL: GET http://localhost:8080/api/orders
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }
}