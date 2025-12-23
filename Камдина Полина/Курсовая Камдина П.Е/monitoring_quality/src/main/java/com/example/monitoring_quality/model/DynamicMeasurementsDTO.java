package com.example.monitoring_quality.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DynamicMeasurementsDTO {
    private String numberRoom;
    private String sensorType;
    private Double measurementValue;
    private LocalDate measurementDate;

    public DynamicMeasurementsDTO() {
    }

    public String getFormattedDateTime() {
        if (measurementDate == null) return "";
        return measurementDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public String getNumberRoom() {
        return numberRoom;
    }

    public void setNumberRoom(String numberRoom) {
        this.numberRoom = numberRoom;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public Double getMeasurementValue() {
        return measurementValue;
    }

    public void setMeasurementValue(Double measurementValue) {
        this.measurementValue = measurementValue;
    }

    public LocalDate getMeasurementDate() {
        return measurementDate;
    }

    public void setMeasurementDate(LocalDate measurementDate) {
        this.measurementDate = measurementDate;
    }
}
