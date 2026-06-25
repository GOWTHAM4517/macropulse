package com.gowtham.macropulse.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gowtham.macropulse.model.CountryGdpHistory;
import com.gowtham.macropulse.service.WorldBankGdpService;

@RestController
public class WorldBankGdpController {

    private final WorldBankGdpService worldBankGdpService;

    public WorldBankGdpController(
            WorldBankGdpService worldBankGdpService) {

        this.worldBankGdpService = worldBankGdpService;
    }

    @GetMapping("/worldbank-gdp")
    public List<CountryGdpHistory> getWorldBankGdp()
            throws IOException {

        return worldBankGdpService.loadGdpData();
    }
}