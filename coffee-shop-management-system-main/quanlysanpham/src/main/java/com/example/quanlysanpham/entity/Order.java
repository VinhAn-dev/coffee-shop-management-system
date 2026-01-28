package com.example.quanlysanpham.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.quanlysanpham.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

// Đại diện cho hóa đơn tổng
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ngày tạo đơn
    @Column(nullable = false)
    private LocalDateTime orderDate;

    // Người tạo đơn (nhân viên)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    // Trạng thái đơn
    @Enumerated(EnumType.STRING) // Lưu chữ "PENDING" vào DB
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    // Danh sách các món trong đơn
    // Tao đổi tên từ orderItems -> items để khớp với OrderService và Getter/Setter
    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    // Tổng tiền
    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    // ===== CONSTRUCTOR =====
    public Order() {}

    public Order(LocalDateTime orderDate, User createdBy, OrderStatus status) {
        this.orderDate = orderDate;
        this.createdBy = createdBy;
        this.status = status;
    }

    // ===== HELPER METHODS (Thêm/Xóa món) =====
    public void addOrderItem(OrderItem item) {
        items.add(item);
        item.setOrder(this); // Quan trọng: Gán order cho item
    }

    public void removeOrderItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);
    }

    // ===== GETTER / SETTER =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    // Hàm này giúp Service gọi order.setUser(...) không bị lỗi
    public void setUser(User staff) {
        this.createdBy = staff;
    }

    public List<OrderItem> getItems() { // Đổi tên getter cho chuẩn
        return items;
    }
    
    // Giữ lại getter cũ nếu code khác có gọi, nhưng trỏ về items
    public List<OrderItem> getOrderItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
        if(items != null) {
            for(OrderItem item : items) item.setOrder(this);
        }
    }
    
    public void setOrderItems(List<OrderItem> items) {
        setItems(items);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        if(status != null){
            this.status = status;
        }
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    // ===== LIFECYCLE CALLBACK (QUAN TRỌNG: Chỉ giữ 1 hàm duy nhất) =====
    // Gộp tất cả logic: Lấy giờ, Set trạng thái, Tính tiền vào đây
    @PrePersist
    @PreUpdate
    protected void onLifecycleEvents() {
        // 1. Tự động lấy giờ nếu chưa có
        if (this.orderDate == null) {
            this.orderDate = LocalDateTime.now();
        }

        // 2. Tự động set trạng thái mặc định
        if (this.status == null) {
            this.status = OrderStatus.PENDING;
        }
    }
}