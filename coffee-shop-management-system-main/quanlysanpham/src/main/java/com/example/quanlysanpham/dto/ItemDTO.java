package com.example.quanlysanpham.dto;

public class ItemDTO {
    private Long productId;
    private int quantity;

    //contructor mac dinh
    public ItemDTO(){}
    //contructor co tham so
    public ItemDTO(Long productId, int quantity){
        this.productId = productId;
        this.quantity = quantity;
    }
    // Getters và Setters
    public Long getProductId() {
        return productId; 
    }
    public void setProductId(Long productId) {
        this.productId = productId; 
    }
    public int getQuantity() {
        return quantity; 
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}