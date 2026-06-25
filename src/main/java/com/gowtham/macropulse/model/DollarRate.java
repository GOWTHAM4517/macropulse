package com.gowtham.macropulse.model;

public class DollarRate {

    private double usdToInr;
    private double previousRate;
    private double changePercent;
    private boolean live;

    public DollarRate(double usdToInr) {
        this.usdToInr = usdToInr;
    }

    public double getUsdToInr() {
        return usdToInr;
    }

    public void setUsdToInr(double usdToInr) {
        this.usdToInr = usdToInr;
    }

    public double getPreviousRate() {
        return previousRate;
    }

    public void setPreviousRate(double previousRate) {
        this.previousRate = previousRate;
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
}
