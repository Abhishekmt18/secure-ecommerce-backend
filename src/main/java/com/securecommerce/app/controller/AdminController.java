package com.securecommerce.app.controller;

import com.securecommerce.app.dto.TransactionResponse;
import com.securecommerce.app.dto.UserResponse;
import com.securecommerce.app.entity.Order;
import com.securecommerce.app.entity.Transaction;
import com.securecommerce.app.repository.OrderRepository;
import com.securecommerce.app.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private OrderRepository orderRepository;

    // 1️⃣ View all transactions
    @GetMapping("/transactions")
    public List<TransactionResponse> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(tx -> new TransactionResponse(
                        tx.getId(),
                        tx.getAmount(),
                        tx.getStatus(),
                        tx.getTransactionTime(),
                        new UserResponse(
                                tx.getUser().getId(),
                                tx.getUser().getName(),
                                tx.getUser().getEmail()
                        )
                ))
                .toList();
    }

    // 2️⃣ View only FRAUD transactions
    @GetMapping("/transactions/fraud")
    public List<TransactionResponse> getFraudTransactions() {
        return transactionRepository.findByStatus("FRAUD")
                .stream()
                .map(tx -> new TransactionResponse(
                        tx.getId(),
                        tx.getAmount(),
                        tx.getStatus(),
                        tx.getTransactionTime(),
                        new UserResponse(
                                tx.getUser().getId(),
                                tx.getUser().getName(),
                                tx.getUser().getEmail()
                        )
                ))
                .toList();
    }

    // 3️⃣ View all orders
    @GetMapping("/orders")
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
