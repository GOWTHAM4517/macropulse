package com.gowtham.macropulse.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.gowtham.macropulse.model.BitcoinPrice;

@Service
public class BitcoinService {

    private static final long CACHE_MS = 60000;
    private static final int MAX_HISTORY = 30;

    private final RestTemplate restTemplate = new RestTemplate();

    private double cachedPrice = 0;
    private double previousPrice = 0;
    private long lastUpdate = 0;
    private final List<Double> history = new ArrayList<>();

    public BitcoinPrice getBitcoinPrice() {

        long now = System.currentTimeMillis();

        if (now - lastUpdate >= CACHE_MS || cachedPrice == 0) {
            refresh(now);
        }

        BitcoinPrice result = new BitcoinPrice(cachedPrice);
        result.setChangePercent(round2(percentChange(previousPrice, cachedPrice)));
        result.setHistory(new ArrayList<>(history));

        return result;
    }

    private void refresh(long now) {
        try {

            String btcUrl =
                "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=usd";

            Map btcResponse =
                restTemplate.getForObject(btcUrl, Map.class);

            Map bitcoin =
                (Map) btcResponse.get("bitcoin");

            double btcUsd =
                Double.parseDouble(
                    bitcoin.get("usd").toString()
                );

            String usdUrl =
                "https://open.er-api.com/v6/latest/USD";

            Map usdResponse =
                restTemplate.getForObject(usdUrl, Map.class);

            Map rates =
                (Map) usdResponse.get("rates");

            double usdInr =
                Double.parseDouble(
                    rates.get("INR").toString()
                );

            double btcInr =
                btcUsd * usdInr;

            if (cachedPrice > 0) {
                previousPrice = cachedPrice;
            }

            cachedPrice = Math.round(btcInr);
            lastUpdate = now;

            pushHistory(cachedPrice);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pushHistory(double value) {
        history.add(value);
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