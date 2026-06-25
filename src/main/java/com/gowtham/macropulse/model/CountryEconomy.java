package com.gowtham.macropulse.model;

public class CountryEconomy {

    private String country;
    private double gdpTrillionUsd;

    public CountryEconomy(String country, double gdpTrillionUsd) {
        this.country = country;
        this.gdpTrillionUsd = gdpTrillionUsd;
    }

    public String getCountry() {
        return country;
    }

    public double getGdpTrillionUsd() {
        return gdpTrillionUsd;
    }
}