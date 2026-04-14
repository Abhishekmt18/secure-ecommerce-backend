package com.securecommerce.app.service;

import com.securecommerce.app.dto.TransactionResponse;
import com.securecommerce.app.dto.UserResponse;
import com.securecommerce.app.entity.Order;
import com.securecommerce.app.repository.OrderRepository;
import com.securecommerce.app.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private OrderRepository orderRepository;

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

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}