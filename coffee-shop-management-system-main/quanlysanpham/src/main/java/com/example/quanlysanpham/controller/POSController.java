package com.example.quanlysanpham.controller;

import com.example.quanlysanpham.dto.CartItem;
import com.example.quanlysanpham.entity.Order;
import com.example.quanlysanpham.entity.OrderItem;
import com.example.quanlysanpham.entity.Product;
import com.example.quanlysanpham.entity.Staff; // Đã import đúng Staff
import com.example.quanlysanpham.repository.OrderRepository;
import com.example.quanlysanpham.repository.ProductRepository;
import com.example.quanlysanpham.repository.StaffRepository;

import jakarta.servlet.http.HttpSession; // 👇 QUAN TRỌNG: Dùng cái này thay cho Principal

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class POSController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private StaffRepository staffRepository;

    @GetMapping("/pos")
    public String showPOS(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "pos"; 
    }

    // --- XỬ LÝ THANH TOÁN (DÙNG SESSION) ---
    @PostMapping("/api/orders/create") 
    @ResponseBody
    // 👇 Thay Principal bằng HttpSession
    public ResponseEntity<String> checkout(@RequestBody List<CartItem> cart, HttpSession session) {
        try {
            if (cart == null || cart.isEmpty()) {
                return ResponseEntity.badRequest().body("Giỏ hàng trống!");
            }

            Order newOrder = new Order();
            newOrder.setOrderDate(LocalDateTime.now());
            
            BigDecimal total = BigDecimal.ZERO;

            // ... (Logic tính tiền giữ nguyên) ...
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

            // 👇 LOGIC MỚI: LẤY TỪ SESSION (Chắc ăn 100%)
            // Key "loggedInUser" phải khớp với file AuthController
            // Ép kiểu về Staff vì mày đang dùng class Staff
            Staff currentUser = (Staff) session.getAttribute("loggedInUser");

            if (currentUser != null) {
                newOrder.setStaff(currentUser);
                System.out.println("DEBUG: Da gan nhan vien: " + currentUser.getFullName());
            } else {
                System.out.println("DEBUG: Session dang trong! (Chua dang nhap)");
                // Nếu muốn bắt buộc đăng nhập mới cho thanh toán thì mở dòng dưới ra:
                // return ResponseEntity.status(401).body("Hết phiên đăng nhập. Hãy login lại!");
            }

            orderRepository.save(newOrder); 

            return ResponseEntity.ok("Xuất Bill thành công! Mã đơn: " + newOrder.getId());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi Server: " + e.getMessage());
        }
    }
}