package com.securecommerce.app.controller;

import com.securecommerce.app.entity.Order;
import com.securecommerce.app.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/checkout/{userId}")
    public String checkout(@PathVariable Long userId,
                           @RequestBody List<Map<String, Object>> items) {

        return orderService.checkout(userId, items);
    }

    @GetMapping("/user/{userId}")
    public List<Order> getOrdersByUser(@PathVariable Long userId) {
        return orderService.getOrders(userId);
    }
}