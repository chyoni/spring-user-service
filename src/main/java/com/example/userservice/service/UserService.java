package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto getUserByUserId(String userId);
    Iterable<User> getAllUser();
}
