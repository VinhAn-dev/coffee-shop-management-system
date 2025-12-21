package com.example.quanlysanpham.repository;

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

    // Lấy các món đang bán
    List<Product> findByIsAvailableTrue();

    // Tìm theo tên (gõ gần đúng)
    List<Product> findByNameContainingIgnoreCase(String keyword);

    // Update giá (để đúng với controller updatePrice trong sơ đồ)
    @Modifying
    @Transactional
    @Query("update Product p set p.price = :newPrice where p.id = :id")
    int updatePrice(@Param("id") Long id, @Param("newPrice") Double newPrice);

    // “Xóa mềm”: chuyển isAvailable = false (thay vì delete thật)
    @Modifying
    @Transactional
    @Query("update Product p set p.isAvailable = false where p.id = :id")
    int softDelete(@Param("id") Long id);
}
