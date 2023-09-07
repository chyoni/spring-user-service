package com.example.userservice.repository;

import com.example.userservice.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUserId(String userId);
    User findByEmail(String email);
}
