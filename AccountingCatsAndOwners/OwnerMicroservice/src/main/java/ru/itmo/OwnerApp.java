package ru.itmo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"ru.itmo"})
public class OwnerApp {
    public static void main(String[] args) {
        SpringApplication.run(OwnerApp.class, args);
    }
}