package com.example.quanlysanpham.dto;

public class CartItem {
    // Sửa tên thành 'productId' để khớp với dữ liệu từ file pos.html gửi lên
    private Long productId; 
    private int quantity;
    
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}