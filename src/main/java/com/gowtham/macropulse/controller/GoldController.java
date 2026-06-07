package com.gowtham.macropulse.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gowtham.macropulse.model.GoldPrice;
import com.gowtham.macropulse.service.GoldService;

@RestController
public class GoldController {

    private final GoldService goldService;

    public GoldController(GoldService goldService) {
        this.goldService = goldService;
    }

    @GetMapping("/gold")
    public GoldPrice getGoldPrice() {
        return goldService.getGoldPrice();
    }
}