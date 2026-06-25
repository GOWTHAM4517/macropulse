package com.gowtham.macropulse.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.gowtham.macropulse.model.NiftyPrice;

/**
 * Live Nifty 50 index value, proxied server-side through Yahoo Finance's
 * public chart endpoint (no API key exists for this - it's the same
 * unofficial-but-widely-used endpoint that powers many free finance
 * dashboards). Calling it from the backend avoids CORS issues and lets
 * us cache it alongside everything else.
 */
@Service
public class NiftyService {

    private static final long CACHE_MS = 60000;
    private static final String NIFTY_URL =
        "https://query1.finance.yahoo.com/v8/finance/chart/%5ENSEI?range=1d&interval=15m";
    private static final int MAX_HISTORY = 30;

    private final RestTemplate restTemplate = new RestTemplate();

    private double cachedPrice = 0;
    private double previousClose = 0;
    private long lastUpdate = 0;
    private boolean lastFetchLive = false;
    private final List<Double> history = new ArrayList<>();

    public NiftyPrice getNiftyPrice() {

        long now = System.currentTimeMillis();

        if (now - lastUpdate >= CACHE_MS) {
            refresh(now);
        }

        double fallback = 24168.00;
        double price = cachedPrice > 0 ? cachedPrice : fallback;
        double previous = previousClose > 0 ? previousClose : price;

        NiftyPrice result = new NiftyPrice(round2(price));
        result.setChangePercent(round2(percentChange(previous, price)));
        result.setLive(lastFetchLive);
        result.setHistory(new ArrayList<>(history));

        return result;
    }

    @SuppressWarnings("unchecked")
    private void refresh(long now) {
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0 (MacroPulse-Dashboard)");
            HttpEntity<Void> request = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                NIFTY_URL, HttpMethod.GET, request, Map.class
            );

            Map<String, Object> chart = (Map<String, Object>) response.getBody().get("chart");
            List<Map<String, Object>> results = (List<Map<String, Object>>) chart.get("result");
            Map<String, Object> result = results.get(0);
            Map<String, Object> meta = (Map<String, Object>) result.get("meta");

            double regularMarketPrice = Double.parseDouble(meta.get("regularMarketPrice").toString());
            double prevClose = Double.parseDouble(meta.get("previousClose").toString());

            cachedPrice = regularMarketPrice;
            previousClose = prevClose;
            lastUpdate = now;
            lastFetchLive = true;

            extractHistory(result);

        } catch (Exception e) {
            e.printStackTrace();
            lastFetchLive = false;
        }
    }

    @SuppressWarnings("unchecked")
    private void extractHistory(Map<String, Object> result) {
        try {

            Map<String, Object> indicators = (Map<String, Object>) result.get("indicators");
            List<Map<String, Object>> quoteList = (List<Map<String, Object>>) indicators.get("quote");
            Map<String, Object> quote = quoteList.get(0);
            List<Object> closes = (List<Object>) quote.get("close");

            history.clear();
            for (Object value : closes) {
                if (value != null) {
                    history.add(round2(Double.parseDouble(value.toString())));
                }
            }

            while (history.size() > MAX_HISTORY) {
                history.remove(0);
            }

        } catch (Exception e) {
            // History is a nice-to-have for the sparkline; ignore failures here.
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
