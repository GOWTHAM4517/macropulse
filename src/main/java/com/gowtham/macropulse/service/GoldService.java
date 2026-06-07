package com.gowtham.macropulse.service;

import org.springframework.stereotype.Service;

import com.gowtham.macropulse.model.GoldPrice;

@Service
public class GoldService {

    public GoldPrice getGoldPrice() {
        return new GoldPrice(
                "Gold",
                "24K",
                9800.00,
                "INR"
        );
    }
}