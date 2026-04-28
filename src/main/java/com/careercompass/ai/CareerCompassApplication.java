package com.careercompass.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CareerCompassApplication {
    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "true");
        System.out.println("🚀 Initializing Career Compass Intelligence Node...");
        SpringApplication.run(CareerCompassApplication.class, args);
    }
}
