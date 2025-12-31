package com.example.quanlysanpham.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.quanlysanpham.entity.Order;
import com.example.quanlysanpham.enums.OrderStatus;

/**
 * Repository cho bảng orders.
 * Dùng để lưu + lấy danh sách hóa đơn.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Lấy tất cả đơn theo userId (staff tạo đơn nào thì xem đơn đó)
    List<Order> findByCreatedById(Long userId);

    // Lấy tất cả đơn theo trạng thái (PENDING/PAID/COMPLETED/CANCELLED)
    List<Order> findByStatus(OrderStatus status);
}
