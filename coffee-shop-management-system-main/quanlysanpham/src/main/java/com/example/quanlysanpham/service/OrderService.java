package com.example.quanlysanpham.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.quanlysanpham.dto.ItemDTO;
import com.example.quanlysanpham.dto.OrderRequest;
import com.example.quanlysanpham.entity.Order;
import com.example.quanlysanpham.entity.OrderItem;
import com.example.quanlysanpham.entity.Product;
import com.example.quanlysanpham.entity.User;
import com.example.quanlysanpham.enums.OrderStatus;
import com.example.quanlysanpham.repository.OrderRepository;
import com.example.quanlysanpham.repository.ProductRepository;
import com.example.quanlysanpham.repository.UserRepository;

@Service // Bắt buộc có để Controller ở trên gọi được
public class OrderService {

    @Autowired private OrderRepository orderRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private ProductRepository productRepo;

    public Order createOrder(OrderRequest request){
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        
        User staff = userRepo.findById(request.getUserId()).orElse(null);
        if (staff == null) {
            throw new RuntimeException("Không tìm thấy nhân viên!");
        }
        order.setCreatedBy(staff);
        // ép kiểu từ String (request) sang Enum (OrderStatus)
        try {
            order.setStatus(OrderStatus.valueOf(request.getStatus())); 
        } catch (Exception e) {
            // Nếu null hoặc gửi sai chữ, mặc định set là PENDING
            order.setStatus(OrderStatus.PENDING);
        }
        // Gọi hàm xử lý với ItemDTO
        processOrderItems(order, request.getItems());

        return orderRepo.save(order);
    }
    
    //ham cap nhat don
    public Order updateOrder(Long id, OrderRequest request) {
        Order existingOrder = orderRepo.findById(id).orElse(null);
        
        if (existingOrder != null) {
            // Cập nhật trạng thái
            try {
                existingOrder.setStatus(OrderStatus.valueOf(request.getStatus()));
            } catch (Exception e) {
                // Giữ nguyên trạng thái cũ hoặc set PENDING tùy logic
            }
            // Xóa món cũ, thêm món mới
            existingOrder.getOrderItems().clear();
            processOrderItems(existingOrder, request.getItems());

            return orderRepo.save(existingOrder);
        }
        return null;
    }

    //ham xu ly mon tinh tien
    private void processOrderItems(Order order, List<ItemDTO> itemDTOs) {
        if (itemDTOs == null) return;

        double total = 0;

        for (ItemDTO dto : itemDTOs) {
            Product product = productRepo.findById(dto.getProductId()).orElse(null);
            
            if (product != null) {
                OrderItem item = new OrderItem();
                item.setProduct(product);
                item.setQuantity(dto.getQuantity());
                item.setPriceAtOrder(product.getPrice()); // Lấy giá gốc từ DB
                
                // Set quan hệ 2 chiều
                order.addOrderItem(item); // Hoặc: item.setOrder(order); order.getItems().add(item);
                
                total += item.getPriceAtOrder().doubleValue() * item.getQuantity();
            }
        }
        // Cập nhật tổng tiền
        order.setTotalAmount(java.math.BigDecimal.valueOf(total));
    }
    public Order getOrderById(Long id) { return orderRepo.findById(id).orElse(null); }
    public List<Order> getAllOrders() { return orderRepo.findAll(); }
}