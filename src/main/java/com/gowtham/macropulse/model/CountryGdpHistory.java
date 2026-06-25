package com.gowtham.macropulse.model;

public class CountryGdpHistory {

    private String country;
    private String countryCode;
    private int year;
    private double gdp;

    public CountryGdpHistory(
            String country,
            String countryCode,
            int year,
            double gdp) {

        this.country = country;
        this.countryCode = countryCode;
        this.year = year;
        this.gdp = gdp;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getGdp() {
        return gdp;
    }

    public void setGdp(double gdp) {
        this.gdp = gdp;
    }
}