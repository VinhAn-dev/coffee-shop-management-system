package com.example.quanlysanpham.controller;

import java.math.BigDecimal;
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
import com.example.quanlysanpham.repository.OrderRepository;
import com.example.quanlysanpham.repository.ProductRepository;

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

    // --- XỬ LÝ THANH TOÁN ---
    // Đường dẫn này khớp với lệnh fetch('/api/orders/create') trong pos.html
    @PostMapping("/api/orders/create") 
    @ResponseBody
    public ResponseEntity<String> checkout(@RequestBody List<CartItem> cart) {
        try {
            if (cart == null || cart.isEmpty()) {
                return ResponseEntity.badRequest().body("Giỏ hàng trống!");
            }

            Order newOrder = new Order();
            BigDecimal total = BigDecimal.ZERO;

            for (CartItem itemDTO : cart) {
                // Sử dụng getProductId() cho đúng với DTO
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
            // Lưu ý: Hiện tại chưa lấy được ID nhân viên từ pos.html nên createdBy sẽ là null
            // (Trong bảng lịch sử sẽ hiện "Không rõ", nhưng vẫn chạy tốt không bị lỗi)
            
            orderRepository.save(newOrder); 

            return ResponseEntity.ok("Xuất Bill thành công! Mã đơn: " + newOrder.getId());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi Server: " + e.getMessage());
        }
    }
}