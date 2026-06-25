package com.gowtham.macropulse.service;

import org.springframework.stereotype.Service;

import com.gowtham.macropulse.model.EconomySummary;

@Service
public class EconomyService {

    public EconomySummary getSummary() {
        return new EconomySummary(
                97.15,
                9800.00,
                110.00
        );
    }
}