package com.example.quanlysanpham.controller;

import com.example.quanlysanpham.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService; // Spring sẽ tìm Bean có @Service ở trên để nhúng vào đây

}