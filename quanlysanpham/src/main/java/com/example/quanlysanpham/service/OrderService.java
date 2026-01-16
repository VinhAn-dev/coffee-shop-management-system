package com.example.quanlysanpham.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.quanlysanpham.entity.Order;
import com.example.quanlysanpham.repository.OrderRepository;

@Service // Bắt buộc có để Controller ở trên gọi được
public class OrderService {

    @Autowired
    private OrderRepository orderRepo;

    // Hàm này dùng để: LƯU ĐƠN HÀNG VÀO DATABASE
    public Order saveOrder(Order order) {
        // (Sau này có thể viết thêm code tính tổng tiền, trừ tồn kho... ở đây)
        return orderRepo.save(order);
    }

    // Hàm này dùng để: LẤY HẾT DỮ LIỆU TỪ BẢNG ORDERS RA
    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }
}