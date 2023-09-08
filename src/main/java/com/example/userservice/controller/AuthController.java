package com.example.userservice.controller;

import com.example.userservice.dto.LoginDto;
import com.example.userservice.dto.UserDto;
import com.example.userservice.service.JwtService;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.ResponseUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody LoginDto loginData) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginData.getEmail(), loginData.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(loginData.getEmail());
        } else {
            throw new UsernameNotFoundException("Invalid user request !");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseUser> createUser(@RequestBody UserDto user) {
        userService.createUser(user);

        ResponseUser responseUser = new ObjectMapper().convertValue(user, ResponseUser.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }
}
