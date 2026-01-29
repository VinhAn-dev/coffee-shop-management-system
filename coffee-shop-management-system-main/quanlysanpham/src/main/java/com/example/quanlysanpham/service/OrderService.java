package com.example.quanlysanpham.service;

import com.example.quanlysanpham.entity.Order;
import com.example.quanlysanpham.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepo;

    // Lấy danh sách tất cả đơn hàng (Sắp xếp mới nhất lên đầu)
    public List<Order> getAllOrders() {
        return orderRepo.findAll(Sort.by(Sort.Direction.DESC, "orderDate"));
    }

    // Lưu đơn hàng (Dùng cho cả việc Tạo mới hoặc Cập nhật sau này)
    public Order saveOrder(Order order) {
        return orderRepo.save(order);
    }
    
    // Lấy chi tiết 1 đơn hàng theo ID
    public Order getOrderById(Long id) {
        return orderRepo.findById(id).orElse(null);
    }
}