package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.Authority;
import com.example.userservice.entity.User;
import com.example.userservice.model.ImplementUser;
import com.example.userservice.repository.AuthorityRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.vo.ResponseOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Environment environment;
    private final RestTemplate restTemplate;

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        userDto.setEncryptedPassword(passwordEncoder.encode(userDto.getPassword()));

        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.convertValue(userDto, User.class);

        userRepository.save(user);
        return mapper.convertValue(user, UserDto.class);
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        User user = userRepository.findByUserId(userId);

        if (user == null)
            throw new UsernameNotFoundException("User not found");

        UserDto userDto = new ObjectMapper().convertValue(user, UserDto.class);

        // List<ResponseOrder> orders = new ArrayList<>();
        String orderUrl = String.format(Objects.requireNonNull(environment.getProperty("order_service.url")), userId);
        ResponseEntity<List<ResponseOrder>> orderListResponse = restTemplate.exchange(
                orderUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        List<ResponseOrder> ordersList = orderListResponse.getBody();

        userDto.setOrders(ordersList);

        return userDto;
    }

    @Override
    public Iterable<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(email + ": is not found");
        }

        ImplementUser newUser = new ImplementUser();
        newUser.setUsername(user.getEmail());
        newUser.setPassword(user.getEncryptedPassword());
        newUser.setAuthorities(getAuthorities(email));
        newUser.setEnabled(true);
        newUser.setAccountNonExpired(true);
        newUser.setAccountNonLocked(true);
        newUser.setCredentialsNonExpired(true);

        return newUser;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String email) {
        List<Authority> authList = authorityRepository.findByEmail(email);
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (Authority authority : authList) {
            authorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
        }
        return authorities;
    }
}
