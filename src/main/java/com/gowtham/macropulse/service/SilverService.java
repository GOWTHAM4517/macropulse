package com.gowtham.macropulse.service;

import org.springframework.stereotype.Service;

import com.gowtham.macropulse.model.SilverPrice;

@Service
public class SilverService {

    public SilverPrice getSilverPrice() {
        return new SilverPrice(120.50, "INR");
    }
}