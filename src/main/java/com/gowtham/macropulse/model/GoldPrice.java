package com.gowtham.macropulse.model;

import java.util.List;

public class GoldPrice {

    private String metal;
    private String purity;
    private double price;
    private String currency;
    private double changePercent;
    private boolean live;
    private List<Double> history;

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

    public double getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(double changePercent) {
        this.changePercent = changePercent;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public List<Double> getHistory() {
        return history;
    }

    public void setHistory(List<Double> history) {
        this.history = history;
    }
}