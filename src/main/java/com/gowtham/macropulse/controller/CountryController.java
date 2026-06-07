package com.gowtham.macropulse.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gowtham.macropulse.model.CountryEconomy;

@RestController
public class CountryController {

    @GetMapping("/countries")
    public List<CountryEconomy> getCountries() {

        return List.of(
                new CountryEconomy("USA", 29.0),
                new CountryEconomy("China", 18.5),
                new CountryEconomy("Germany", 4.7),
                new CountryEconomy("Japan", 4.1),
                new CountryEconomy("India", 4.0)
        );
    }
}