package com.gowtham.macropulse.service;

import org.springframework.stereotype.Service;

import com.gowtham.macropulse.model.DollarRate;

@Service
public class CurrencyService {

    public DollarRate getDollarRate() {
        return new DollarRate(97.15);
    }
}