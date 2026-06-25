package com.gowtham.macropulse.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gowtham.macropulse.model.BitcoinPrice;
import com.gowtham.macropulse.service.BitcoinService;

@RestController
public class BitcoinController {

    private final BitcoinService bitcoinService;

    public BitcoinController(BitcoinService bitcoinService) {
        this.bitcoinService = bitcoinService;
    }

    @GetMapping("/bitcoin")
    public BitcoinPrice getBitcoinPrice() {
        return bitcoinService.getBitcoinPrice();
    }
}