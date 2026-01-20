package com.securecommerce.app.repository;

import com.securecommerce.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User, Long>{

    User findByEmail(String email);
}
