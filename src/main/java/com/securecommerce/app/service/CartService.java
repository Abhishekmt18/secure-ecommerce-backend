package com.securecommerce.app.service;

import com.securecommerce.app.entity.*;
import com.securecommerce.app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public String addToCart(Long userId, Long productId, int quantity) {

        User user = userRepository.findById(userId).orElse(null);
        Product product = productRepository.findById(productId).orElse(null);

        if (user == null || product == null) {
            return "User or Product not found";
        }

        CartItem item = new CartItem();
        item.setUser(user);
        item.setProduct(product);
        item.setQuantity(quantity);

        cartItemRepository.save(item);

        return "Product added to cart";
    }

    public List<CartItem> viewCart(Long userId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) return List.of();

        return cartItemRepository.findByUser(user);
    }
}