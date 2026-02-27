package com.securecommerce.app.repository;

import com.securecommerce.app.entity.Transaction;
import com.securecommerce.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByStatus(String status);

    List<Transaction> findByUser(User user);
}
