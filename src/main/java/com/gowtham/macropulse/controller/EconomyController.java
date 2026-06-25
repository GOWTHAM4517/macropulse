package com.gowtham.macropulse.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gowtham.macropulse.model.EconomySummary;
import com.gowtham.macropulse.service.EconomyService;

@RestController
public class EconomyController {

    private final EconomyService economyService;

    public EconomyController(EconomyService economyService) {
        this.economyService = economyService;
    }

    @GetMapping("/economy")
    public EconomySummary getEconomy() {
        return economyService.getSummary();
    }
}