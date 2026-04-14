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
import com.securecommerce.app.service.OrderService;
import com.securecommerce.app.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartItemRepository cartItemRepository;

    @PostMapping("/send")
    public String sendOtp(@RequestParam String email) {
        otpService.sendOtp(email);
        return "OTP sent";
    }

    @PostMapping("/verify")
    public String verifyOtp(@RequestParam String email,
                            @RequestParam String otp) {

        boolean isValid = otpService.verifyOtp(email, otp);

        if (!isValid) {
            return "INVALID_OTP";
        }

        return orderService.verifyOtpAndPlaceOrder(email);
    }
}
