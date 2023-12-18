package com.c.p.repository;

import com.c.p.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User>findUserByUsername(String username);
    User findUserByEmail(String email);
}
