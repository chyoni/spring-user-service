package com.example.userservice.dto;

import com.example.userservice.vo.ResponseOrder;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class UserDto {
    private String email;
    private String name;
    private String password;
    private String userId;
    private Date createdAt;

    private String encryptedPassword;

    private List<ResponseOrder> orders;
}
