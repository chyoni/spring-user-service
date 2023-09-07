package com.example.userservice.repository;

import com.example.userservice.entity.Authority;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuthorityRepository extends CrudRepository<Authority, Long> {
    List<Authority> findByEmail(String email);
}
