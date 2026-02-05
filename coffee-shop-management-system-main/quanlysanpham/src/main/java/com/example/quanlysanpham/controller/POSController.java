package com.example.quanlysanpham.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.quanlysanpham.dto.CartItem;
import com.example.quanlysanpham.entity.Order;
import com.example.quanlysanpham.entity.OrderItem;
import com.example.quanlysanpham.entity.Product;
import com.example.quanlysanpham.entity.User;
import com.example.quanlysanpham.repository.OrderRepository;
import com.example.quanlysanpham.repository.ProductRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class POSController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/pos")
    public String showPOS(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "pos"; 
    }

    // Bắt buộc phải có để in bill
    @GetMapping("/order/print/{id}")
    public String printBill(@PathVariable Long id, Model model) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            model.addAttribute("order", order);
            return "print-bill"; 
        }
        return "redirect:/pos";
    }

    // XỬ LÝ THANH TOÁN
    @PostMapping("/api/orders/create") 
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkout(@RequestBody List<CartItem> cart, HttpSession session) {
        try {
            // 1. Kiểm tra giỏ hàng
            if (cart == null || cart.isEmpty()) {
                Map<String, Object> err = new HashMap<>();
                err.put("status", "error");
                err.put("message", "Giỏ hàng trống!");
                return ResponseEntity.badRequest().body(err);
            }

            // 2. Tạo đơn hàng
            Order newOrder = new Order();
            newOrder.setOrderDate(LocalDateTime.now());
            
            BigDecimal total = BigDecimal.ZERO;

            for (CartItem itemDTO : cart) {
                Product product = productRepository.findById(itemDTO.getProductId()).orElse(null);
                if (product != null) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProduct(product);
                    orderItem.setQuantity(itemDTO.getQuantity());
                    orderItem.setPriceAtOrder(product.getPrice());
                    
                    // Gắn order vào item
                    orderItem.setOrder(newOrder); 
                    newOrder.getItems().add(orderItem); 
                    
                    BigDecimal lineTotal = product.getPrice().multiply(new BigDecimal(itemDTO.getQuantity()));
                    total = total.add(lineTotal);
                }
            }
            newOrder.setTotalAmount(total);

            // 3. Xử lý Session
            User currentUser = (User) session.getAttribute("user");
            if (currentUser == null) {
                currentUser = (User) session.getAttribute("loggedInUser");
            }

            if (currentUser != null) {
                newOrder.setStaff(currentUser);
            } else {
                System.out.println("DEBUG: Không tìm thấy nhân viên trong Session!");
            }
            
            // 1. Lưu và gán kết quả vào biến savedOrder
            Order savedOrder = orderRepository.save(newOrder); 

            // 2. Chuẩn bị dữ liệu trả về
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Thanh toán thành công!");
            
            // 3. Lấy ID từ biến savedOrder (Chắc chắn có ID)
            response.put("orderId", savedOrder.getId());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> err = new HashMap<>();
            err.put("status", "error");
            err.put("message", "Lỗi Server: " + e.getMessage());
            return ResponseEntity.status(500).body(err);
        }
    }
}