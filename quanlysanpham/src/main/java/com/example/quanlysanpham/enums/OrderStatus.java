package com.example.quanlysanpham.enums;

import java.util.EnumSet;

public enum OrderStatus {
    PENDING,    // chờ xử lý / chờ làm
    PAID,       // đã thanh toán
    COMPLETED,  // đã hoàn thành
    CANCELLED;  // đã hủy

    public boolean canTransitionTo(OrderStatus next) {
        if (next == null) return false;

        return switch (this) {
            case PENDING -> EnumSet.of(PAID, COMPLETED, CANCELLED).contains(next);
            case PAID -> EnumSet.of(COMPLETED).contains(next);
            case COMPLETED, CANCELLED -> false;
            default -> false;
        };
    }
}

