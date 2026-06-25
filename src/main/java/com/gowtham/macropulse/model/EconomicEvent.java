package com.gowtham.macropulse.model;

public class EconomicEvent {

    private String time;
    private String country;
    private String event;

    public EconomicEvent(
            String time,
            String country,
            String event) {

        this.time = time;
        this.country = country;
        this.event = event;
    }

    public String getTime() {
        return time;
    }

    public String getCountry() {
        return country;
    }

    public String getEvent() {
        return event;
    }
}