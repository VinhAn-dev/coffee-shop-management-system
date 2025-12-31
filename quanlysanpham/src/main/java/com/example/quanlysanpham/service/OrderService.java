package com.example.quanlysanpham.service;

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

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    /**
     * staffId lấy từ token để biết ai tạo đơn.
     * req.items là danh sách món + số lượng.
     */
    public OrderResponse createOrder(Long staffId, OrderRequest req) {
        if (req == null || req.getItems() == null || req.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order items is empty");
        }

        // staff phải tồn tại
        User staff = userRepository.findById(staffId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Staff not found"));

        // chặn tài khoản không phải STAFF tạo đơn
        if (staff.getRole() == null || !staff.getRole().equalsIgnoreCase("STAFF")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only STAFF can create order");
        }

        Order order = new Order();
        order.setCreatedBy(staff);
        order.setStatus(OrderStatus.PENDING);

        List<ItemDTO> items = req.getItems();
        for (ItemDTO dto : items) {
            if (dto == null) continue;

            Long productId = dto.getProductId(); // id món
            Integer qty = dto.getQuantity();     // số lượng mua

            if (productId == null || qty == null || qty <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid productId/quantity");
            }

            Product p = productRepository.findById(productId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Product not found: " + productId));

            // món phải còn bán
            if (p.getIsAvailable() == null || !p.getIsAvailable()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Product is not available: " + productId);
            }

            OrderItem oi = new OrderItem();
            oi.setProduct(p);
            oi.setQuantity(qty);
            oi.setPriceAtOrder(p.getPrice()); // chốt giá tại thời điểm mua

            // addItem() tự set order + tự cộng tổng tiền
            order.addItem(oi);
        }

        Order saved = orderRepository.save(order);

        return new OrderResponse(
                saved.getId(),
                saved.getCreatedDate(),
                saved.getTotalAmount(),
                saved.getStatus()
        );
    }

    /**
     * Staff đổi trạng thái đơn.
     * staff chỉ được sửa đơn do staff đó tạo.
     */
    public OrderResponse updateStatus(Long staffId, Long orderId, UpdateStatusRequest req) {
        if (orderId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing orderId");
        }
        if (req == null || req.getStatus() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing status");
        }

        // staff phải tồn tại
        User staff = userRepository.findById(staffId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Staff not found"));

        if (staff.getRole() == null || !staff.getRole().equalsIgnoreCase("STAFF")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only STAFF can update status");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        // chặn sửa đơn người khác
        if (order.getCreatedBy() == null || order.getCreatedBy().getId() == null
                || !order.getCreatedBy().getId().equals(staffId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only update your own orders");
        }

        OrderStatus newStatus = req.getStatus();
        OrderStatus current = order.getStatus();

        // chặn chuyển trạng thái kiểu “tùm lum” (đơn giản cho MVP)
        if (!isValidTransition(current, newStatus)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid status transition: " + current + " -> " + newStatus);
        }

        order.setStatus(newStatus);
        Order saved = orderRepository.save(order);

        return new OrderResponse(
                saved.getId(),
                saved.getCreatedDate(),
                saved.getTotalAmount(),
                saved.getStatus()
        );
    }

    /**
     * Staff xem lịch sử đơn của chính mình.
     */
    public List<OrderResponse> getMyOrders(Long staffId) {
        User staff = userRepository.findById(staffId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Staff not found"));

        if (staff.getRole() == null || !staff.getRole().equalsIgnoreCase("STAFF")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only STAFF can view orders here");
        }

        return orderRepository.findByCreatedById(staffId).stream()
                .map(o -> new OrderResponse(o.getId(), o.getCreatedDate(), o.getTotalAmount(), o.getStatus()))
                .collect(Collectors.toList());
    }

    // check luồng trạng thái cơ bản
    private boolean isValidTransition(OrderStatus current, OrderStatus next) {
        if (current == null) return true;

        // đã COMPLETED/CANCELLED thì thôi
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

    public List<OrderResponse> getAllOrders(Long staffId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
