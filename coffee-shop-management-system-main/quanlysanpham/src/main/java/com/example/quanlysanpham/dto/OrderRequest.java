package com.example.quanlysanpham.dto;
import java.util.List;

public class OrderRequest {
    private Long userId;
    private List<ItemDTO> items;
    private String status;

    // Getters và Setters
    public Long getUserId() { 
        return userId; 
    }
    public void setUserId(Long userId) {
        this.userId = userId; 
    }

    public List<ItemDTO> getItems() {
        return items; 
    }
    public void setItems(List<ItemDTO> items) { 
        this.items = items; 
    }
    
    public String getStatus(){
        return status;
    }
    public void setStatus(String Status){
        this.status = status;
    }
}