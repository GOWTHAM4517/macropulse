package com.gowtham.macropulse.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gowtham.macropulse.model.SilverPrice;
import com.gowtham.macropulse.service.SilverService;

@RestController
public class SilverController {

    private final SilverService silverService;

    public SilverController(SilverService silverService) {
        this.silverService = silverService;
    }

    @GetMapping("/silver")
    public SilverPrice getSilverPrice() {
        return silverService.getSilverPrice();
    }
}