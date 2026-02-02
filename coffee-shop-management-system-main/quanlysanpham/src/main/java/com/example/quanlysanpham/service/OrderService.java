package com.example.quanlysanpham.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.quanlysanpham.entity.Order;
import com.example.quanlysanpham.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepo;

    // Lấy danh sách tất cả đơn hàng (Sắp xếp mới nhất lên đầu)
    public List<Order> getAllOrders() {
        return orderRepo.findAll(Sort.by(Sort.Direction.DESC, "orderDate"));
    }

    // Lưu đơn hàng
    public Order saveOrder(Order order) {
        return orderRepo.save(order);
    }
    
    // Lấy chi tiết 1 đơn hàng theo ID
    public Order getOrderById(Long id) {
        return orderRepo.findById(id).orElse(null);
    }

    public Page<Order> getOrdersWithFilter(LocalDate date, String staffName, Pageable pageable) {
        LocalDate searchDate = (date != null) ? date : LocalDate.now();
        
        LocalDateTime startOfDay = searchDate.atStartOfDay();
        LocalDateTime endOfDay = searchDate.atTime(LocalTime.MAX);


        String searchName;
        if (staffName != null && !staffName.trim().isEmpty()) {
            searchName = "%" + staffName.trim() + "%"; 
        } else {
            searchName = null; 
        }

        // 3. Gọi Repository
        return orderRepo.findByOrderDateBetweenAndUser_FullNameContainingIgnoreCase(
                startOfDay, endOfDay, searchName, pageable);
    }
}