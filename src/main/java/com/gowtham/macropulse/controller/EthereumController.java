package com.gowtham.macropulse.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gowtham.macropulse.model.EthereumPrice;
import com.gowtham.macropulse.service.EthereumService;

@RestController
public class EthereumController {

    private final EthereumService ethereumService;

    public EthereumController(EthereumService ethereumService) {
        this.ethereumService = ethereumService;
    }

    @GetMapping("/ethereum")
    public EthereumPrice getEthereumPrice() {
        return ethereumService.getEthereumPrice();
    }
}
