package com.example.quanlysanpham.entity;

import com.example.quanlysanpham.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // tránh trùng keyword "order" trong SQL
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // LocalDateTime createdDate
    private LocalDateTime createdDate;

    // Double totalAmount
    private Double totalAmount;

    // OrderStatus status
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // created by: 1 Order thuộc về 1 User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // FK tới User.id
    private User createdBy;

    // contains many: 1 Order có nhiều OrderItem
    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference // tránh vòng lặp JSON nếu OrderItem có @JsonBackReference
    private List<OrderItem> items = new ArrayList<>();

    // ========= Constructors =========
    public Order() {
    }

    // ========= JPA lifecycle =========
    @PrePersist
    protected void onCreate() {
        if (this.createdDate == null) this.createdDate = LocalDateTime.now();
        if (this.status == null) this.status = OrderStatus.PENDING;
        if (this.totalAmount == null) this.totalAmount = 0.0;
    }

    // ========= Helper methods (giữ quan hệ 2 chiều đúng) =========
    public void addItem(OrderItem item) {
        if (item == null) return;
        items.add(item);
        item.setOrder(this);
        recalculateTotalAmount();
    }

    public void removeItem(OrderItem item) {
        if (item == null) return;
        items.remove(item);
        item.setOrder(null);
        recalculateTotalAmount();
    }

    public void recalculateTotalAmount() {
        double sum = 0.0;
        for (OrderItem it : items) {
            if (it == null) continue;
            Integer qty = it.getQuantity();
            Double price = it.getPriceAtOrder();
            if (qty != null && price != null) {
                sum += qty * price;
            }
        }
        this.totalAmount = sum;
    }

    // ========= Getters/Setters =========
    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = (items == null) ? new ArrayList<>() : items;
        // đảm bảo set back-reference + cập nhật tổng tiền
        for (OrderItem it : this.items) {
            if (it != null) it.setOrder(this);
        }
        recalculateTotalAmount();
    }
}
