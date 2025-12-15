package edu.espe.sensoresbiblioteca.model;

import java.time.Instant;

public class SensorReading {
    private String id;
    private double temperature;
    private Instant timestamp;

    public SensorReading(double temperature, String id, Instant timestamp) {
        this.temperature = temperature;
        this.id = id;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public double getTemperature() {
        return temperature;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
