package com.example.quanlysanpham.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.quanlysanpham.dto.ItemDTO;
import com.example.quanlysanpham.dto.OrderRequest;
import com.example.quanlysanpham.dto.OrderResponse;
import com.example.quanlysanpham.dto.UpdateStatusRequest;
import com.example.quanlysanpham.entity.Order;
import com.example.quanlysanpham.entity.OrderItem;
import com.example.quanlysanpham.entity.Product;
import com.example.quanlysanpham.entity.User;
import com.example.quanlysanpham.enums.OrderStatus;
import com.example.quanlysanpham.repository.OrderRepository;
import com.example.quanlysanpham.repository.ProductRepository;
import com.example.quanlysanpham.repository.UserRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(
            OrderRepository orderRepository,
            ProductRepository productRepository,
            UserRepository userRepository
    ) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // Staff tạo order mới (createdBy = staff đang login)
    public OrderResponse createOrder(Long staffId, OrderRequest req) {
        if (staffId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing token");
        }
        if (req == null || req.getItems() == null || req.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order items is empty");
        }

        User staff = requireStaff(staffId);

        Order order = new Order();
        order.setCreatedBy(staff);

        // orderDate trong entity là NOT NULL => bắt buộc set
        order.setOrderDate(LocalDateTime.now());

        // status mặc định PENDING rồi nhưng set rõ cho dễ đọc
        order.setStatus(OrderStatus.PENDING);

        for (ItemDTO dto : req.getItems()) {
            if (dto == null) continue;

            Long productId = dto.getProductId();
            Integer quantity = dto.getQuantity();

            if (productId == null || quantity == null || quantity <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid productId/quantity");
            }

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found: " + productId));

            if (product.getIsAvailable() == null || !product.getIsAvailable()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product is not available: " + productId);
            }

            // Tạo OrderItem và gắn vào Order bằng hàm addOrderItem()
            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(quantity);

            // Nếu product.getPrice() null thì coi như 0 để tránh crash
            BigDecimal priceAtOrder = toBigDecimal(product.getPrice());
            item.setPriceAtOrder(priceAtOrder);

            // Quan hệ 2 chiều: addOrderItem sẽ setOrder(this)
            order.addOrderItem(item);
        }

        // PrePersist/PreUpdate trong Order sẽ tự calculateTotalAmount trước khi lưu
        Order saved = orderRepository.save(order);

        return toResponse(saved);
    }

    // Staff xem đơn do mình tạo
    public List<OrderResponse> getMyOrders(Long staffId) {
        if (staffId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing token");
        }

        requireStaff(staffId);

        return orderRepository.findByCreatedBy_Id(staffId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Staff đổi status đơn (chỉ được đổi đơn do mình tạo)
    public OrderResponse updateStatus(Long staffId, Long orderId, UpdateStatusRequest req) {
        if (staffId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing token");
        }
        if (orderId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing orderId");
        }
        if (req == null || req.getStatus() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing status");
        }

        requireStaff(staffId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        // Chỉ cho đổi đơn do chính staff tạo
        if (order.getCreatedBy() == null || order.getCreatedBy().getId() == null
                || !order.getCreatedBy().getId().equals(staffId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only update your own orders");
        }

        OrderStatus current = order.getStatus();
        OrderStatus next = req.getStatus();

        if (!isValidTransition(current, next)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status transition: " + current + " -> " + next);
        }

        order.setStatus(next);
        Order saved = orderRepository.save(order);

        return toResponse(saved);
    }

    // ===== helper =====

    private User requireStaff(Long staffId) {
        User staff = userRepository.findById(staffId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        if (staff.getRole() == null || !staff.getRole().equalsIgnoreCase("STAFF")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only STAFF allowed");
        }
        return staff;
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderDate(),
                order.getTotalAmount(),
                order.getStatus()
        );
    }

    private BigDecimal toBigDecimal(Double value) {
        if (value == null) return BigDecimal.ZERO;
        return BigDecimal.valueOf(value);
    }

    // Luồng trạng thái đơn cho staff (MVP)
    private boolean isValidTransition(OrderStatus current, OrderStatus next) {
        if (current == null) return true;

        // Đã COMPLETED hoặc CANCELLED thì khóa
        if (current == OrderStatus.COMPLETED || current == OrderStatus.CANCELLED) return false;

        // PENDING -> PAID hoặc CANCELLED
        if (current == OrderStatus.PENDING) {
            return next == OrderStatus.PAID || next == OrderStatus.CANCELLED;
        }

        // PAID -> COMPLETED hoặc CANCELLED
        if (current == OrderStatus.PAID) {
            return next == OrderStatus.COMPLETED || next == OrderStatus.CANCELLED;
        }

        return false;
    }
}
