package com.example.quanlysanpham.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.quanlysanpham.entity.Order;
import com.example.quanlysanpham.enums.OrderStatus;
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
    

    //Hàm này dùng để: LẤY ĐƠN HÀNG THEO ID
    public Order getOrderById(Long id) {
        return orderRepo.findById(id).orElse(null);
    }
    // Hàm này dùng để: CẬP NHẬT ĐƠN HÀNG (CHỈ CẬP NHẬT KHI TRẠNG THÁI LÀ PENDING)
    public Order updateOrder(Long id, Order updatedOrder) {
        Order existingOrder = orderRepo.findById(id).orElse(null);
        if (existingOrder != null && existingOrder.getStatus() == OrderStatus.PENDING) {
            // Cập nhật các thông tin cần thiết, ví dụ: danh sách món ăn
            existingOrder.setId(updatedOrder.getId());
            // Có thể tính toán lại tổng tiền ở đây nếu cần
            return orderRepo.save(existingOrder);
        }
        return null;
    }

}