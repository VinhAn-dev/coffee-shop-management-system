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

    // SỬA: Thay findByIsAvailableTrue() bằng findAll()
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    // SỬA: Thay softDelete() bằng deleteById() (Xóa luôn khỏi DB)
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}