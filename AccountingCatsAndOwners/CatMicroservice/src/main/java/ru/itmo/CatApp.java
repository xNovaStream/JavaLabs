package ru.itmo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"ru.itmo"})
public class CatApp {
    public static void main(String[] args) {
        SpringApplication.run(CatApp.class, args);
    }
}