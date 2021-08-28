package com.stepanov.uocns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.stepanov.uocns")
public class UocnsApplication {

    public static void main(String[] args) {
        SpringApplication.run(UocnsApplication.class, args);
    }

}
