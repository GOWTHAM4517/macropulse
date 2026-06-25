package com.gowtham.macropulse.model;

import java.util.List;

public class NiftyPrice {

    private double price;
    private double changePercent;
    private boolean live;
    private List<Double> history;

    public NiftyPrice(double price) {
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
