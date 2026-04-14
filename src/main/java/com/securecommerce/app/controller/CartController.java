package com.securecommerce.app.controller;

import com.securecommerce.app.entity.*;
import com.securecommerce.app.repository.*;
import com.securecommerce.app.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartService cartService;

    // Add to cart
    @PostMapping("/add")
    public String addToCart(@RequestParam Long userId,
                            @RequestParam Long productId,
                            @RequestParam int quantity) {

        return cartService.addToCart(userId, productId, quantity);
    }

    @GetMapping("/{userId}")
    public List<CartItem> viewCart(@PathVariable Long userId) {
        return cartService.viewCart(userId);
    }
}
