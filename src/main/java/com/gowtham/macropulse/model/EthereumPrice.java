package com.gowtham.macropulse.model;

import java.util.List;

public class EthereumPrice {

    private double priceInr;
    private double priceUsd;
    private double changePercent;
    private boolean live;
    private List<Double> history;

    public EthereumPrice(double priceInr, double priceUsd) {
        this.priceInr = priceInr;
        this.priceUsd = priceUsd;
    }

    public double getPriceInr() {
        return priceInr;
    }

    public double getPriceUsd() {
        return priceUsd;
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
