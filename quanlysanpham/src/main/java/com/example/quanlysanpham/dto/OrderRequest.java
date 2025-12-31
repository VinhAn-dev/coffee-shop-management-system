package com.example.quanlysanpham.dto;

import java.util.List;

public class OrderRequest {

    //danh sách món khách chọn + số lượng
    private List<ItemDTO> items;

    public List<ItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }
}
