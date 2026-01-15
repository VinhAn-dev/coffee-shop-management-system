package com.example.quanlysanpham.dto;
import java.util.List;

public class OrderRequest {
    private Long userId;
    private List<ItemDTO> items;

    // Getters và Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public List<ItemDTO> getItems() { return items; }
    public void setItems(List<ItemDTO> items) { this.items = items; }
}