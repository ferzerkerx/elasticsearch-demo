package com.ferzerkerx.elasticsearch.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventData {
    private final double amount;
    private final long timeStamp;

    public EventData(double amount) {
        this(amount, System.currentTimeMillis());
    }

    public EventData(double amount, long timeStamp) {
        this.amount = amount;
        this.timeStamp = timeStamp;
    }

    public double getAmount() {
        return amount;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String toString() {
        return "EventData{" +
                "amount=" + amount +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
