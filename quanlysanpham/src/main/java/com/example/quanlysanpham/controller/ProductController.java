package com.example.quanlysanpham.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.quanlysanpham.entity.Product;
import com.example.quanlysanpham.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String listProducts(Model model) {
        // Lấy danh sách sản phẩm từ Service và gửi sang giao diện
        model.addAttribute("products", productService.getAllProducts());
        return "product-list"; 
    }
    // 2. Hiển thị form thêm mới
    @GetMapping("/new")
    public String showNewForm(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "product-form"; 
    }

    // 3. Xử lý lưu sản phẩm (cho cả Thêm mới và Sửa)
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") Product product) {
        productService.saveProduct(product);
        return "redirect:/products"; // Lưu xong quay về trang danh sách
    }

    // 4. Hiển thị form sửa sản phẩm
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "product-form"; 
    }

    // 5. Xóa sản phẩm
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}