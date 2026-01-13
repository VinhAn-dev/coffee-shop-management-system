package com.example.quanlysanpham.repository;

import java.math.BigDecimal; // <--- Nhớ import cái này
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.quanlysanpham.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // 1. Chỉ lấy các món đang bán (isAvailable = true)
    // (Dùng để hiển thị menu cho khách, tránh hiện món đã ẩn)
    List<Product> findByIsAvailableTrue();

    // 2. Tìm theo tên (gõ gần đúng, không phân biệt hoa thường)
    List<Product> findByNameContainingIgnoreCase(String keyword);

    // 3. Update giá
    // ⚠️ QUAN TRỌNG: Đã đổi Double thành BigDecimal để khớp với Entity
    @Modifying
    @Transactional
    @Query("update Product p set p.price = :newPrice where p.id = :id")
    int updatePrice(@Param("id") Long id, @Param("newPrice") BigDecimal newPrice);

    // 4. "Xóa mềm": chuyển isAvailable = false (Ẩn món đi chứ không xóa mất)
    @Modifying
    @Transactional
    @Query("update Product p set p.isAvailable = false where p.id = :id")
    int softDelete(@Param("id") Long id);
}