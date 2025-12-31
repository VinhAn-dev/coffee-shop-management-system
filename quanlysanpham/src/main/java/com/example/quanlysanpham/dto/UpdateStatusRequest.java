package com.example.quanlysanpham.dto;

import com.example.quanlysanpham.enums.OrderStatus;

public class UpdateStatusRequest {
    private OrderStatus status;

    public OrderStatus getStatus() {
        return status;
    }
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
