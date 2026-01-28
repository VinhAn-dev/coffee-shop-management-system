package com.example.quanlysanpham.repository;

import com.example.quanlysanpham.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Không cần viết gì thêm, JpaRepository lo hết rồi
}