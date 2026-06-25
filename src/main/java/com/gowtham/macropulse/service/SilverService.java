package com.gowtham.macropulse.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.gowtham.macropulse.model.SilverPrice;

/**
 * Live silver price, sourced from gold-api.com (free, no key, no rate limit).
 * Price is converted from USD/troy-oz to INR/gram using the live USD-INR rate
 * from CurrencyService, so it auto-updates with both sources.
 */
@Service
public class SilverService {

    private static final long CACHE_MS = 60000;
    private static final String SILVER_URL = "https://api.gold-api.com/price/XAG";
    private static final double TROY_OZ_TO_GRAM = 31.1035;
    private static final int MAX_HISTORY = 30;

    private final RestTemplate restTemplate = new RestTemplate();
    private final CurrencyService currencyService;

    private double cachedPricePerGramInr = 0;
    private double previousPricePerGramInr = 0;
    private long lastUpdate = 0;
    private boolean lastFetchLive = false;
    private final List<Double> history = new ArrayList<>();

    public SilverService(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    public SilverPrice getSilverPrice() {

        long now = System.currentTimeMillis();

        if (now - lastUpdate >= CACHE_MS) {
            refresh(now);
        }

        double fallback = 120.50;
        double price = cachedPricePerGramInr > 0 ? cachedPricePerGramInr : fallback;
        double previous = previousPricePerGramInr > 0 ? previousPricePerGramInr : price;

        SilverPrice result = new SilverPrice(round2(price), "INR");
        result.setChangePercent(round2(percentChange(previous, price)));
        result.setLive(lastFetchLive);
        result.setHistory(new ArrayList<>(history));

        return result;
    }

    private void refresh(long now) {
        try {

            Map<?, ?> response = restTemplate.getForObject(SILVER_URL, Map.class);
            double pricePerOzUsd = Double.parseDouble(response.get("price").toString());

            double usdToInr = currencyService.getCurrentUsdToInr();
            double pricePerGramInr = (pricePerOzUsd * usdToInr) / TROY_OZ_TO_GRAM;

            if (cachedPricePerGramInr > 0) {
                previousPricePerGramInr = cachedPricePerGramInr;
            }

            cachedPricePerGramInr = pricePerGramInr;
            lastUpdate = now;
            lastFetchLive = true;

            pushHistory(pricePerGramInr);

        } catch (Exception e) {
            e.printStackTrace();
            lastFetchLive = false;
        }
    }

    private void pushHistory(double value) {
        history.add(round2(value));
        while (history.size() > MAX_HISTORY) {
            history.remove(0);
        }
    }

    private double percentChange(double previous, double current) {
        if (previous <= 0) return 0;
        return ((current - previous) / previous) * 100;
    }

    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}