package com.securecommerce.app.repository;

import com.securecommerce.app.entity.CartItem;
import com.securecommerce.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
}
