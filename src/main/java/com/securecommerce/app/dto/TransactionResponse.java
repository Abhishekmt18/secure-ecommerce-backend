package com.securecommerce.app.dto;

import java.time.LocalDateTime;

public class TransactionResponse {

    private Long id;
    private double amount;
    private String status;
    private LocalDateTime transactionTime;
    private UserResponse user;

    public TransactionResponse(Long id, double amount, String status,
                               LocalDateTime transactionTime, UserResponse user) {
        this.id = id;
        this.amount = amount;
        this.status = status;
        this.transactionTime = transactionTime;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public UserResponse getUser() {
        return user;
    }
}
