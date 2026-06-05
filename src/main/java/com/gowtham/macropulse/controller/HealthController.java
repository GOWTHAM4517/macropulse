package com.gowtham.macropulse.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/")
    public String home() {
        return "MacroPulse Running";
    }

    @GetMapping("/health")
    public String health() {
        return "API Healthy";
    }
}