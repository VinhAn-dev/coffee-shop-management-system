package com.example.quanlysanpham.repository;

import java.math.BigDecimal;
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

    // 1. Tìm theo tên (Cái này an toàn, giữ lại dùng rất tiện)
    List<Product> findByNameContainingIgnoreCase(String keyword);

    // 2. Update giá (Cái này cũng an toàn, giữ lại)
    @Modifying
    @Transactional
    @Query("update Product p set p.price = :newPrice where p.id = :id")
    int updatePrice(@Param("id") Long id, @Param("newPrice") BigDecimal newPrice);

    // --- ĐÃ XÓA CÁC HÀM 'isAvailable' ĐỂ SERVER CHẠY ĐƯỢC ---
    // (Vì Product hiện tại không có cột isAvailable nên phải xóa đi mới hết lỗi)
}