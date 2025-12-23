package com.example.monitoring_quality.model;

public class RoomStatusDTO {
    private String roomNumber;
    private Double co2Value;
    private Double temperature;
    private Double humidity;
    
    // Геттеры и сеттеры
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    
    public Double getCo2Value() { return co2Value; }
    public void setCo2Value(Double co2Value) { this.co2Value = co2Value; }
    
    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }
    
    public Double getHumidity() { return humidity; }
    public void setHumidity(Double humidity) { this.humidity = humidity; }

    public String getFormattedCo2() {
        if (co2Value == null) return "Нет данных";
        return String.format("%.0f", co2Value);
    }
    
    public String getFormattedTemperature() {
        if (temperature == null) return "Нет данных";
        return String.format("%.1f°C", temperature);
    }
    
    public String getFormattedHumidity() {
        if (humidity == null) return "Нет данных";
        return String.format("%.1f%%", humidity);
    }
    
    public String getOverallStatus() {
        if (co2Value != null && co2Value >= 1500) return "Критический (CO₂)";
        if (temperature != null && (temperature >= 28 || temperature <= 16)) return "Критический (Темп.)";
        if (humidity != null && (humidity >= 70 || humidity <= 30)) return "Критический (Влаж.)";
        
        if (co2Value != null && co2Value >= 1100) return "CO₂ высокий";
        if (temperature != null && (temperature >= 26 || temperature <= 18)) return "Температура";
        if (humidity != null && (humidity >= 65 || humidity <= 35)) return "Влажность";
        
        return "Норма";
    }
}