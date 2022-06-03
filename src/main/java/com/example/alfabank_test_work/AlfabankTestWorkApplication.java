package com.example.alfabank_test_work;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AlfabankTestWorkApplication {
    public static void main(String[] args) {
        SpringApplication.run(AlfabankTestWorkApplication.class, args);
    }
}