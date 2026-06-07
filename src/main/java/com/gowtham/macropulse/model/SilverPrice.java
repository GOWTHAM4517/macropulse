package com.gowtham.macropulse.model;

public class SilverPrice {

    private double price;
    private String currency;

    public SilverPrice(double price, String currency) {
        this.price = price;
        this.currency = currency;
    }

    public double getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }
}