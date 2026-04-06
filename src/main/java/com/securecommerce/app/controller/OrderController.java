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
import com.securecommerce.app.service.OtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

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

    @Autowired
    private OtpService otpService;

    @PostMapping("/checkout/{userId}")
    public String checkout(@PathVariable Long userId, @RequestBody List<Map<String, Object>> items) {

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return "User not found";

        double amount = 0;

        for (Map<String, Object> item : items) {
            double price = Double.parseDouble(item.get("price").toString());
            int quantity = Integer.parseInt(item.get("quantity").toString());

            amount += price * quantity;   // ✅ FIX
        }

        logger.info("Total calculated amount = {}", amount);

        FraudRequest request = new FraudRequest();
        request.setAmount(amount);

        FraudResponse response = restTemplate.postForObject(
                "http://localhost:5000/fraud/check",
                request,
                FraudResponse.class
        );

        String risk = response.getResult();
        logger.info("Fraud risk for user {} is {}", userId, risk);

        if (risk.equals("LOW")) {

            logger.info("LOW risk transaction for user {}", userId);

            Order order = new Order();
            order.setTotalAmount(amount);
            order.setUser(user);
            orderRepository.save(order);

            Transaction txn = new Transaction();
            txn.setAmount(amount);
            txn.setStatus(TransactionStatus.SUCCESS.name());
            txn.setUser(user);
            transactionRepository.save(txn);

            // cartItemRepository.deleteAll(items);

            return "LOW risk - Order placed successfully";
        }
        else if (risk.equals("MEDIUM")) {

            logger.info("MEDIUM risk - OTP triggered for user {}", userId);

            otpService.sendOtp(user.getEmail());

            return "MEDIUM risk - OTP sent";
        }
        else if (risk.equals("HIGH")) {

            logger.info("HIGH risk - Biometric triggered for user {}", userId);

            String bioResponse = restTemplate.postForObject(
                    "http://localhost:5000/biometric/verify",
                    null,
                    String.class
            );

            if (!bioResponse.contains("VERIFIED")) {
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

            // cartItemRepository.deleteAll(items);

            return "HIGH risk - Biometric verified. Order placed.";
        }

        return "Unable to process transaction";
    }
    @GetMapping("/user/{userId}")
    public List<Order> getOrdersByUser(@PathVariable Long userId) {
        return orderRepository.findByUserId(userId);
    }
}