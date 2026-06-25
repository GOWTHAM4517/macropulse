package com.gowtham.macropulse.model;

import java.util.List;

public class BitcoinPrice {

    private double price;
    private double changePercent;
    private List<Double> history;

    public BitcoinPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public double getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(double changePercent) {
        this.changePercent = changePercent;
    }

    public List<Double> getHistory() {
        return history;
    }

    public void setHistory(List<Double> history) {
        this.history = history;
    }
}