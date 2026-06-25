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
        new CountryEconomy("India", 4.0),
        new CountryEconomy("United Kingdom", 3.6),
        new CountryEconomy("France", 3.2),
        new CountryEconomy("Italy", 2.4),
        new CountryEconomy("Canada", 2.3),
        new CountryEconomy("Brazil", 2.2),
        new CountryEconomy("Russia", 2.1),
        new CountryEconomy("South Korea", 1.9),
        new CountryEconomy("Australia", 1.8),
        new CountryEconomy("Spain", 1.7),
        new CountryEconomy("Mexico", 1.6),
        new CountryEconomy("Indonesia", 1.5),
        new CountryEconomy("Netherlands", 1.2),
        new CountryEconomy("Saudi Arabia", 1.1),
        new CountryEconomy("Turkey", 1.0),
        new CountryEconomy("Switzerland", 0.95)

    );

    }
}