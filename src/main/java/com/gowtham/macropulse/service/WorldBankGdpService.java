package com.gowtham.macropulse.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.gowtham.macropulse.model.CountryGdpHistory;

@Service
public class WorldBankGdpService {

    public List<CountryGdpHistory> loadGdpData()
            throws IOException {

        List<CountryGdpHistory> result =
                new ArrayList<>();

        InputStream inputStream =
                new ClassPathResource(
                        "data/worldbank-gdp.csv")
                        .getInputStream();

        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(inputStream));

        String line;

        // Find header row automatically
        while ((line = reader.readLine()) != null) {

            if (line.contains("Country Name")
                    && line.contains("Country Code")) {
                break;
            }
        }

        // Read country rows
        while ((line = reader.readLine()) != null) {

            String[] cols =
line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

for (int i = 0; i < cols.length; i++) {
    cols[i] = cols[i]
            .replace("\"", "")
            .trim();
}

            if (cols.length < 10)
                continue;

            String country = cols[0].trim();
            String countryCode = cols[1].trim();

            if (country.isBlank()
                    || countryCode.isBlank())
                continue;

            String c =
                    country.toLowerCase();

            // Remove regions and aggregates
            if (
                    c.contains("world") ||
                    c.contains("income") ||
                    c.contains("oecd") ||
                    c.contains("euro area") ||
                    c.contains("sub-saharan") ||
                    c.contains("africa eastern") ||
                    c.contains("africa western") ||
                    c.contains("least developed") ||
                    c.contains("fragile") ||
                    c.contains("arab world") ||
                    c.contains("latin america") ||
                    c.contains("caribbean") ||
                    c.contains("north america") ||
                    c.contains("middle east") ||
                    c.contains("europe") ||
                    c.contains("asia") ||
                    c.contains("ida") ||
                    c.contains("ibrd") ||
                    c.contains("hipc") ||
                    c.contains("small states") ||
                    c.contains("classification") ||
                    c.contains("dividend") ||
                    c.contains("members")
            ) {
                continue;
            }

            for (int year = 1960; year <= 2024; year++) {

                int index = year - 1956;

                if (index >= cols.length)
                    continue;

                String value = cols[index];

                if (value == null
                        || value.isBlank())
                    continue;

                try {

                    double gdp =
                            Double.parseDouble(value)
                                    / 1_000_000_000_000.0;

                    result.add(
                            new CountryGdpHistory(
                                    country,
                                    countryCode,
                                    year,
                                    gdp));

                } catch (Exception e) {
                    // ignore invalid values
                }
            }
        }

        reader.close();

        System.out.println(
                "GDP Records Loaded = "
                        + result.size());

        return result;
    }
}