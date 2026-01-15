package com.example.quanlysanpham.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // số lượng món trong hóa đơn
    @Column(nullable = false)
    private Integer quantity;

    // giá của món tại thời điểm tạo đơn (để sau này đổi giá thì đơn cũ vẫn đúng)
    @Column(nullable = false)
    private BigDecimal priceAtOrder;

    // nhiều OrderItem thuộc về 1 Order
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // nhiều OrderItem có thể trỏ tới cùng 1 Product
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public OrderItem() {}

    public OrderItem(Order order, Product product, Integer quantity, BigDecimal priceAtOrder) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.priceAtOrder = priceAtOrder;
    }

    public Long getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPriceAtOrder() {
        return priceAtOrder;
    }

    public void setPriceAtOrder(BigDecimal priceAtOrder) {
        this.priceAtOrder = priceAtOrder;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    //tính bill bằng cách lấy giá của sản phẩm * số lượng
    //chuyển quantity từ int sang Bigdecimal để tính toán cho đúng
    @Transient
    public BigDecimal getLineTotal() {
        if (priceAtOrder == null || quantity == null) return BigDecimal.ZERO;
        return priceAtOrder.multiply(new BigDecimal(quantity));
    }
}
