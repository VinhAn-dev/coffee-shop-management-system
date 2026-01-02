package com.example.quanlysanpham.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.quanlysanpham.dto.OrderRequest;
import com.example.quanlysanpham.dto.OrderResponse;
import com.example.quanlysanpham.dto.UpdateStatusRequest;
import com.example.quanlysanpham.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/staff")
public class StaffOrderController {

    private final OrderService orderService;

    // orderService: lớp xử lý nghiệp vụ liên quan tới Order (tạo đơn, xem đơn, đổi trạng thái)
    public StaffOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Staff tạo đơn hàng mới.
     * - request body: danh sách món và số lượng (items)
     * - staffId: lấy từ token sau khi filter xác thực và set vào request attribute
     * Trả về: thông tin đơn vừa tạo (id, createdDate, totalAmount, status)
     */
    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> createOrder(
            @RequestBody OrderRequest req,
            HttpServletRequest request
    ) {
        Long staffId = (Long) request.getAttribute("authUserId");
        OrderResponse res = orderService.createOrder(staffId, req);
        return ResponseEntity.ok(res);
    }

    /**
     * Staff xem danh sách đơn hàng (phục vụ kiểm tra nhanh sau khi create).
     * - staffId: lấy từ token
     * Trả về: list các đơn
     */
    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getAllOrders(HttpServletRequest request) {
        Long staffId = (Long) request.getAttribute("authUserId");
        List<OrderResponse> res = orderService.getMyOrders(staffId);
        return ResponseEntity.ok(res);
    }

    /**
     * Staff đổi trạng thái đơn.
     * - id: id của order trên URL
     * - req.status: trạng thái mới (PENDING/PAID/COMPLETED/CANCELLED)
     * - staffId: lấy từ token
     */
    @PatchMapping("/orders/{id}/status")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable("id") Long id,
            @RequestBody UpdateStatusRequest req,
            HttpServletRequest request
    ) {
        Long staffId = (Long) request.getAttribute("authUserId");
        OrderResponse res = orderService.updateStatus(staffId, id, req);
        return ResponseEntity.ok(res);
    }
}
