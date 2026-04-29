package com.careercompass.ai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

/**
 * Health check endpoint for deployment monitoring.
 * Returns server status and timestamp for load balancers, uptime monitors, and CI/CD pipelines.
 */
@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "Career Compass AI Backend",
                "timestamp", Instant.now().toString(),
                "version", "1.0.0"
        ));
    }
}
