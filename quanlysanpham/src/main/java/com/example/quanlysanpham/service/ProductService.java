package com.example.quanlysanpham.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.quanlysanpham.entity.Product;
import com.example.quanlysanpham.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Hàm lấy tất cả sản phẩm (Controller đang tìm cái này)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Hàm lưu sản phẩm (Controller đang tìm cái này)
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    // Hàm lấy sản phẩm theo ID
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    // Hàm xóa sản phẩm
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}