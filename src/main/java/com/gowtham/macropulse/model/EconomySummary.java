package com.gowtham.macropulse.model;

public class EconomySummary {

    private double usdToInr;
    private double goldPrice;
    private double silverPrice;

    public EconomySummary(double usdToInr, double goldPrice, double silverPrice) {
        this.usdToInr = usdToInr;
        this.goldPrice = goldPrice;
        this.silverPrice = silverPrice;
    }

    public double getUsdToInr() {
        return usdToInr;
    }

    public double getGoldPrice() {
        return goldPrice;
    }

    public double getSilverPrice() {
        return silverPrice;
    }
}