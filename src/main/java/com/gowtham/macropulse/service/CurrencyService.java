package com.gowtham.macropulse.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.gowtham.macropulse.model.DollarRate;

@Service
public class CurrencyService {

    private static final long CACHE_MS = 60000;
    private static final String RATE_URL = "https://open.er-api.com/v6/latest/USD";

    private final RestTemplate restTemplate = new RestTemplate();

    private double cachedRate = 0;
    private double previousRate = 0;
    private long lastUpdate = 0;
    private boolean lastFetchLive = false;

    public DollarRate getDollarRate() {

        long now = System.currentTimeMillis();

        if (now - lastUpdate < CACHE_MS && cachedRate > 0) {
            return buildResponse();
        }

        try {

            Map<?, ?> response = restTemplate.getForObject(RATE_URL, Map.class);
            Map<?, ?> rates = (Map<?, ?>) response.get("rates");
            double inrRate = Double.parseDouble(rates.get("INR").toString());

            if (cachedRate > 0) {
                previousRate = cachedRate;
            }

            cachedRate = inrRate;
            lastUpdate = now;
            lastFetchLive = true;

        } catch (Exception e) {
            e.printStackTrace();
            lastFetchLive = false;
        }

        return buildResponse();
    }

    private DollarRate buildResponse() {

        double fallback = 97.15;
        double current = cachedRate > 0 ? cachedRate : fallback;
        double previous = previousRate > 0 ? previousRate : current;

        DollarRate rate = new DollarRate(current);
        rate.setPreviousRate(previous);
        rate.setChangePercent(percentChange(previous, current));
        rate.setLive(lastFetchLive);

        return rate;
    }

    private double percentChange(double previous, double current) {
        if (previous <= 0) return 0;
        return ((current - previous) / previous) * 100;
    }

    /** Used by other services (Gold/Silver/Ethereum) that need USD-&gt;INR for conversion. */
    public double getCurrentUsdToInr() {
        getDollarRate();
        return cachedRate > 0 ? cachedRate : 97.15;
    }
}