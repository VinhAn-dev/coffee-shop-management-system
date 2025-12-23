package com.example.quanlysanpham;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import com.example.quanlysanpham.entity.Order;
import com.example.quanlysanpham.entity.OrderItem;
import com.example.quanlysanpham.entity.Product;
import com.example.quanlysanpham.entity.User;
import com.example.quanlysanpham.enums.OrderStatus;
import com.example.quanlysanpham.repository.OrderRepository;
import com.example.quanlysanpham.repository.ProductRepository;
import com.example.quanlysanpham.repository.UserRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public DataSeeder(UserRepository userRepository,
                      ProductRepository productRepository,
                      OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void run(String... args) {

        // 1) Test UserRepository
        User admin = new User("admin", "123", "ADMIN", "Admin Demo");
        admin = userRepository.save(admin);
        System.out.println("Saved user id = " + admin.getId());

        System.out.println("Find by username 'admin' = " +
                userRepository.findByUsername("admin").isPresent());

        // 2) Test ProductRepository
        Product p1 = productRepository.save(new Product("Ca phe sua", 25000.0, true));
        Product p2 = productRepository.save(new Product("Latte", 30000.0, true));
        System.out.println("Products count = " + productRepository.count());

        System.out.println("Available products = " + productRepository.findByIsAvailableTrue().size());
        System.out.println("Search 'lat' = " + productRepository.findByNameContainingIgnoreCase("lat").size());

        // 3) Test OrderRepository (quan hệ Order - OrderItem - Product - User)
        Order order = new Order();
        order.setCreatedBy(admin);
        order.setStatus(OrderStatus.PENDING);

        // tạo 2 item (priceAtOrder lấy theo product hiện tại)
        OrderItem i1 = new OrderItem();
        i1.setProduct(p1);
        i1.setQuantity(2);
        i1.setPriceAtOrder(BigDecimal.valueOf(p1.getPrice()));
        order.addOrderItem(i1);

        OrderItem i2 = new OrderItem();
        i2.setProduct(p2);
        i2.setQuantity(1);
        i2.setPriceAtOrder(BigDecimal.valueOf(p2.getPrice()));
        order.addOrderItem(i2);

        // totalAmount đã được order.recalculateTotalAmount() gọi trong addItem
        order = orderRepository.save(order);

        System.out.println("Saved order id = " + order.getId()
                + ", total = " + order.getTotalAmount()
                + ", items = " + order.getOrderItems().size());

        System.out.println("Orders count = " + orderRepository.count());
    }
}
