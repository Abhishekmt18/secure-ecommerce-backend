package com.securecommerce.app.service;

import com.securecommerce.app.entity.User;
import com.securecommerce.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String register(User user) {

        if (userRepository.findByEmail(user.getEmail()) != null) {
            return "Email already exists";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");

        userRepository.save(user);

        return "User registered successfully";
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}