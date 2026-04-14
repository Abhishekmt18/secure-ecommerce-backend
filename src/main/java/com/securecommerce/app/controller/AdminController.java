package com.securecommerce.app.controller;

import com.securecommerce.app.dto.TransactionResponse;
import com.securecommerce.app.entity.Order;
import com.securecommerce.app.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/transactions")
    public List<TransactionResponse> getAllTransactions() {
        return adminService.getAllTransactions();
    }

    @GetMapping("/transactions/fraud")
    public List<TransactionResponse> getFraudTransactions() {
        return adminService.getFraudTransactions();
    }

    @GetMapping("/orders")
    public List<Order> getAllOrders() {
        return adminService.getAllOrders();
    }

    @GetMapping("/products")
    public String getAllProductsForAdmin() {
        return "Admin: All products accessed";
    }
}