package com.example.quanlysanpham.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // tên món
    @Column(nullable = false)
    private String name;

    // giá bán
    @Column(nullable = false)
    private Double price;

    // còn bán / hết hàng
    @Column(nullable = false)
    private Boolean isAvailable = true;

    public Product() {
    }

    public Product(String name, Double price, Boolean isAvailable) {
        this.name = name;
        this.price = price;
        this.isAvailable = isAvailable;
    }

    // ===== Getter/Setter =====
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setIsAvailable(Boolean available) {
        isAvailable = available;
    }
}
