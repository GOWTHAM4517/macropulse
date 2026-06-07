package com.gowtham.macropulse.model;

public class GoldPrice {

    private String metal;
    private String purity;
    private double price;
    private String currency;

    public GoldPrice(String metal, String purity, double price, String currency) {
        this.metal = metal;
        this.purity = purity;
        this.price = price;
        this.currency = currency;
    }

    public String getMetal() {
        return metal;
    }

    public String getPurity() {
        return purity;
    }

    public double getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }
}