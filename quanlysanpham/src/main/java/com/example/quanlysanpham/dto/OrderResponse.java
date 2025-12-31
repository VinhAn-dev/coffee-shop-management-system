package com.example.quanlysanpham.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.quanlysanpham.enums.OrderStatus;

public class OrderResponse {
    private Long id;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private OrderStatus status;

    public OrderResponse() {}

    public OrderResponse(Long id, LocalDateTime orderDate, BigDecimal totalAmount, OrderStatus status) {
        this.id = id;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public Long getId() { return id; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public OrderStatus getStatus() { return status; }
}
