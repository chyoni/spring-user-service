package com.example.userservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
class UserServiceApplicationTests {

    @Test
    void contextLoads() {

        String a = "*ㅗ공단ㅗ*";
        String[] split = a.split("\\*ㅗ공단ㅗ\\*", -1);
        System.out.println("a.split(\"\\\\*ㅗ공단ㅗ\\\\*\", -1) : " + split.length);
    }

}
