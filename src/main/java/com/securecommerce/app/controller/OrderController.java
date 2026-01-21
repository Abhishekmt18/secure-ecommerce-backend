package com.securecommerce.app.controller;

import com.securecommerce.app.entity.*;
import com.securecommerce.app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @PostMapping("/checkout/{userId}")
    public String checkout(@PathVariable Long userId) {

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return "User not found";

        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        if (cartItems.isEmpty()) return "Cart is empty";

        double totalAmount = 0;
        for (CartItem item : cartItems) {
            totalAmount += item.getProduct().getPrice() * item.getQuantity();
        }

        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(totalAmount);
        order.setStatus("CREATED");
        order.setCreatedAt(LocalDateTime.now());
        orderRepository.save(order);

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setAmount(totalAmount);
        transaction.setStatus("SUCCESS"); // later AI will decide
        transaction.setTransactionTime(LocalDateTime.now());
        transactionRepository.save(transaction);

        cartItemRepository.deleteAll(cartItems);

        return "Order placed successfully";
    }
}
