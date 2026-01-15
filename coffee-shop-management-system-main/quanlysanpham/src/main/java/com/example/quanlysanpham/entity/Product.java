package com.example.quanlysanpham.entity;

import java.math.BigDecimal;

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

    private String name;
    private String description;
    private BigDecimal price; // Dùng BigDecimal để tính tiền cho chính xác
    private String imageUrl;
    
    private boolean isAvailable = true; 

    public Product() {}

    // Constructor tiện lợi để tạo nhanh object (Dùng trong DatabaseLoader)
    public Product(String name, String description, BigDecimal price, String imageUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isAvailable = true;
    }

    // --- Getters và Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    //HÀM NÀY QUAN TRỌNG: Giúp mày nhập số double (vd: 25000.0) nó tự đổi sang BigDecimal
    public void setPrice(double d) {
        this.price = BigDecimal.valueOf(d);
    }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    // Sửa lại tên hàm này cho đúng chuẩn Java (isAvailable)
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean isAvailable) { this.isAvailable = isAvailable; }
}