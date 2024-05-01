package ru.itmo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"ru.itmo"})
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}