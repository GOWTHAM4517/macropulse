package com.gowtham.macropulse.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gowtham.macropulse.model.DollarRate;
import com.gowtham.macropulse.service.CurrencyService;

@RestController
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/usd")
    public DollarRate getDollarRate() {
        return currencyService.getDollarRate();
    }
}