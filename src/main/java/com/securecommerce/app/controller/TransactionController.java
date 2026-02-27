package com.securecommerce.app.controller;

import com.securecommerce.app.entity.Transaction;
import com.securecommerce.app.entity.User;
import com.securecommerce.app.repository.TransactionRepository;
import com.securecommerce.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{userId}")
    public List<Transaction> getTransactions(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return List.of();
        return transactionRepository.findByUser(user);
    }
}
