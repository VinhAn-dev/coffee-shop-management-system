package com.example.quanlysanpham.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.quanlysanpham.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Đã sửa: dùng "o.staff" khớp với biến trong Order.java của bạn
    @Query("SELECT o FROM Order o LEFT JOIN o.staff s WHERE " +
           "(o.orderDate BETWEEN :start AND :end) " +
           "AND (:staffName IS NULL OR LOWER(s.fullName) LIKE LOWER(:staffName))")
    Page<Order> findByOrderDateBetweenAndUser_FullNameContainingIgnoreCase(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("staffName") String staffName,
            Pageable pageable
    );
}