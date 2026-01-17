package com.example.quanlysanpham.controller;

import com.example.quanlysanpham.entity.Order;
import com.example.quanlysanpham.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders") // Định nghĩa: Mọi link API trong này đều bắt đầu bằng /api/orders
@CrossOrigin(origins = "*")    // Cho phép Frontend kết nối vào mà không bị chặn
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Hàm này dùng để: TẠO ĐƠN HÀNG MỚI
    // (Được gọi khi khách bấm nút "Thanh toán" ở Frontend)
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Order newOrder) {
        try {
            Order savedOrder = orderService.saveOrder(newOrder);
            return ResponseEntity.ok(savedOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi tạo đơn hàng: " + e.getMessage());
        }
    }

    // Hàm này dùng để: LẤY DANH SÁCH TẤT CẢ ĐƠN HÀNG
    // (Được gọi khi Admin vào trang Quản lý đơn hàng để xem lịch sử)
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }


    
    // Hàm này dùng để: CHỈNH SỬA ĐƠN HÀNG (KHI TRẠNG THÁI LÀ PENDING)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody Order updatedOrder) {
        try {
            Order result = orderService.updateOrder(id, updatedOrder);
            if (result != null) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body("Không thể chỉnh sửa đơn hàng đã hoàn thành hoặc không tồn tại.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi chỉnh sửa: " + e.getMessage());
        }
    }

    
}