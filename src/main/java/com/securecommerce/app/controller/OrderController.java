package com.securecommerce.app.controller;

import com.securecommerce.app.entity.CartItem;
import com.securecommerce.app.entity.Order;
import com.securecommerce.app.entity.Transaction;
import com.securecommerce.app.entity.User;
import com.securecommerce.app.enums.TransactionStatus;
import com.securecommerce.app.repository.CartItemRepository;
import com.securecommerce.app.repository.OrderRepository;
import com.securecommerce.app.repository.TransactionRepository;
import com.securecommerce.app.repository.UserRepository;
import com.securecommerce.app.dto.FraudRequest;
import com.securecommerce.app.dto.FraudResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CartItemRepository cartItemRepository;

    @PostMapping("/checkout/{userId}")
    public String checkout(@PathVariable Long userId) {

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return "User not found";

        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        double amount = 0;
        for (CartItem item : cartItems) {
            amount += item.getProduct().getPrice() * item.getQuantity();
        }

        FraudRequest request = new FraudRequest();
        request.setAmount(amount);

        FraudResponse response = restTemplate.postForObject(
                "http://localhost:5000/fraud/check",
                request,
                FraudResponse.class
        );

        String risk = response.getResult();

        if (risk.equals("HIGH")) {

            Transaction txn = new Transaction();
            txn.setAmount(amount);
            txn.setStatus(TransactionStatus.FRAUD.name());
            txn.setUser(user);
            transactionRepository.save(txn);

            return "Transaction blocked due to HIGH fraud risk";
        }

        else if (risk.equals("MEDIUM")) {

            // Simulate biometric verification
            String biometricResponse = restTemplate.postForObject(
                    "http://localhost:5000/biometric/verify",
                    null,
                    String.class
            );

            if (!biometricResponse.contains("VERIFIED")) {
                return "Biometric verification failed";
            }

            Order order = new Order();
            order.setTotalAmount(amount);
            order.setUser(user);
            orderRepository.save(order);

            Transaction txn = new Transaction();
            txn.setAmount(amount);
            txn.setStatus(TransactionStatus.SUCCESS.name());
            txn.setUser(user);
            transactionRepository.save(txn);
            cartItemRepository.deleteAll(cartItems);

            return "Biometric Authentication Successful - Order Placed";
        }

        else { // LOW

            Order order = new Order();
            order.setTotalAmount(amount);
            order.setUser(user);
            orderRepository.save(order);

            Transaction txn = new Transaction();
            txn.setAmount(amount);
            txn.setStatus(TransactionStatus.SUCCESS.name());
            txn.setUser(user);
            transactionRepository.save(txn);
            cartItemRepository.deleteAll(cartItems);

            return "Order placed successfully (LOW risk)";
        }
    }
}