package com.example.quanlysanpham.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.quanlysanpham.dto.CartItem;
import com.example.quanlysanpham.entity.Order;
import com.example.quanlysanpham.entity.OrderItem;
import com.example.quanlysanpham.entity.Product;
import com.example.quanlysanpham.entity.Staff;
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

    @PostMapping("/api/orders/create") 
    @ResponseBody
    public ResponseEntity<String> checkout(@RequestBody List<CartItem> cart, HttpSession session) {
        try {
            if (cart == null || cart.isEmpty()) {
                return ResponseEntity.badRequest().body("Giỏ hàng trống!");
            }

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
                    newOrder.addOrderItem(orderItem); 
                    
                    BigDecimal lineTotal = product.getPrice().multiply(new BigDecimal(itemDTO.getQuantity()));
                    total = total.add(lineTotal);
                }
            }
            newOrder.setTotalAmount(total);

            // --- KIỂM TRA SESSION VÀ GÁN NHÂN VIÊN ---
            // Thử lấy với cả 2 key phổ biến: "user" và "loggedInUser" để tránh nhầm lẫn với AuthController
            Staff currentUser = (Staff) session.getAttribute("user");
            if (currentUser == null) {
                currentUser = (Staff) session.getAttribute("loggedInUser");
            }

            if (currentUser != null) {
                newOrder.setStaff(currentUser); // Gán vào biến staff trong Order.java
                System.out.println("DEBUG: Thanh toán bởi nhân viên: " + currentUser.getFullName());
            } else {
                System.out.println("DEBUG: Không tìm thấy nhân viên trong Session!");
                // Bạn có thể trả về lỗi 401 tại đây nếu muốn bắt buộc đăng nhập
            }

            orderRepository.save(newOrder); 

            return ResponseEntity.ok("Xuất Bill thành công! Mã đơn: " + newOrder.getId());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi Server: " + e.getMessage());
        }
    }
}