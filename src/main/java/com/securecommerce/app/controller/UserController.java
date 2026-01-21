package com.securecommerce.app.controller;


import com.securecommerce.app.entity.User;
import com.securecommerce.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public User registerUser(@RequestBody User user){
        return userRepository.save(user);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody User loginRequest) {

        User user = userRepository.findByEmail(loginRequest.getEmail());

        if (user == null) {
            return "User not found";
        }

        if (!user.getPassword().equals(loginRequest.getPassword())) {
            return "Invalid password";
        }

        return "Login successful";
    }

}
