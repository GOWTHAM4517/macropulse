package com.gowtham.macropulse.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.gowtham.macropulse.model.EthereumPrice;

/**
 * Live Ethereum price, sourced from gold-api.com (free, no key, no rate limit).
 * Same provider already used for Gold/Silver, kept consistent on purpose.
 */
@Service
public class EthereumService {

    private static final long CACHE_MS = 60000;
    private static final String ETH_URL = "https://api.gold-api.com/price/ETH";
    private static final int MAX_HISTORY = 30;

    private final RestTemplate restTemplate = new RestTemplate();
    private final CurrencyService currencyService;

    private double cachedPriceUsd = 0;
    private double previousPriceUsd = 0;
    private long lastUpdate = 0;
    private boolean lastFetchLive = false;
    private final List<Double> history = new ArrayList<>();

    public EthereumService(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    public EthereumPrice getEthereumPrice() {

        long now = System.currentTimeMillis();

        if (now - lastUpdate >= CACHE_MS) {
            refresh(now);
        }

        double fallback = 3500.00;
        double priceUsd = cachedPriceUsd > 0 ? cachedPriceUsd : fallback;
        double previous = previousPriceUsd > 0 ? previousPriceUsd : priceUsd;

        double usdToInr = currencyService.getCurrentUsdToInr();
        double priceInr = priceUsd * usdToInr;

        EthereumPrice result = new EthereumPrice(round2(priceInr), round2(priceUsd));
        result.setChangePercent(round2(percentChange(previous, priceUsd)));
        result.setLive(lastFetchLive);
        result.setHistory(new ArrayList<>(history));

        return result;
    }

    private void refresh(long now) {
        try {

            Map<?, ?> response = restTemplate.getForObject(ETH_URL, Map.class);
            double priceUsd = Double.parseDouble(response.get("price").toString());

            if (cachedPriceUsd > 0) {
                previousPriceUsd = cachedPriceUsd;
            }

            cachedPriceUsd = priceUsd;
            lastUpdate = now;
            lastFetchLive = true;

            pushHistory(priceUsd);

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
