package com.example.quanlysanpham.service;

import org.springframework.stereotype.Service; // Quan trọng: Thêm dòng này
import com.example.quanlysanpham.dto.OrderRequest;
import com.example.quanlysanpham.entity.Order;
import java.util.List;

@Service // Quan trọng: Đánh dấu class này là một Service
public class OrderService {
    
    // Bạn cần triển khai các hàm này để Controller có thể gọi được
    public Order createOrder(OrderRequest request) {
        // Code xử lý tạo đơn hàng ở đây
        return new Order(); 
    }

    public List<Order> getAllOrders() {
        // Code lấy danh sách đơn hàng ở đây
        return List.of();
    }
}