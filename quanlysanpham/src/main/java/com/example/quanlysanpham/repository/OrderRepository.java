package com.example.quanlysanpham.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.quanlysanpham.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // lấy đơn theo userId (vì entity đang dùng createdBy)
    List<Order> findByCreatedById(Long userId);
}
