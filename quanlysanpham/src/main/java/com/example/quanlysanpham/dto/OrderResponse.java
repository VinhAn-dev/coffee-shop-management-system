package com.example.quanlysanpham.dto;

import java.time.LocalDateTime;

import com.example.quanlysanpham.enums.OrderStatus;

public class OrderResponse {
    private final Long id;
    private final LocalDateTime createdDate;
    private final Double totalAmount;
    private final OrderStatus status;

    public OrderResponse(Long id, LocalDateTime createdDate, Double totalAmount, OrderStatus status) {
        this.id = id;
        this.createdDate = createdDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public Long getId() { return id; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public Double getTotalAmount() { return totalAmount; }
    public OrderStatus getStatus() { return status; }
}
