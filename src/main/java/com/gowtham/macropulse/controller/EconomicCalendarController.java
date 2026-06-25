package com.gowtham.macropulse.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gowtham.macropulse.model.EconomicEvent;

@RestController
public class EconomicCalendarController {

    @GetMapping("/economic-calendar")
    public List<EconomicEvent> getEvents() {

        return List.of(

            new EconomicEvent(
                "09:00",
                "India",
                "Consumer Price Index"
            ),

            new EconomicEvent(
                "10:00",
                "USA",
                "GDP Growth Rate"
            ),

            new EconomicEvent(
                "14:00",
                "China",
                "Manufacturing PMI"
            ),

            new EconomicEvent(
                "16:00",
                "Germany",
                "Inflation Rate"
            ),

            new EconomicEvent(
                "18:00",
                "Japan",
                "Interest Rate Decision"
            )
        );
    }
}