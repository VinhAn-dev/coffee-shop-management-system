package com.example.quanlysanpham.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.quanlysanpham.entity.Product;
import com.example.quanlysanpham.service.ProductService;

@RestController // ⚠️ Phải dùng cái này cho API, đừng dùng @Controller
@RequestMapping("/api/products") // Đường dẫn chuẩn API
@CrossOrigin(origins = "*")      // Mở cửa cho Frontend gọi vào
public class ProductController {

    @Autowired
    private ProductService productService;

    // 1. Lấy danh sách sản phẩm (Frontend gọi GET /api/products)
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // 2. Thêm mới hoặc Sửa (Frontend gọi POST /api/products)
    @PostMapping
    public ResponseEntity<String> saveProduct(@RequestBody Product product) {
        // Log kiểm tra xem dữ liệu có tới nơi không
        System.out.println(">>> Đang lưu sản phẩm: " + product.getName());
        
        productService.saveProduct(product);
        return ResponseEntity.ok("Đã lưu thành công!");
    }

    // 3. Lấy chi tiết 1 sản phẩm (để sửa)
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product p = productService.getProductById(id);
        if(p != null) return ResponseEntity.ok(p);
        return ResponseEntity.notFound().build();
    }

    // 4. Xóa sản phẩm
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Đã xóa thành công!");
    }
}