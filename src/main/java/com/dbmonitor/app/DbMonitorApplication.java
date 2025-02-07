package com.dbmonitor.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DbMonitorApplication {
    public static void main(String[] args) {
        SpringApplication.run(DbMonitorApplication.class, args);
        System.out.println("Database Monitoring Web Portal is running...");
    }
}
