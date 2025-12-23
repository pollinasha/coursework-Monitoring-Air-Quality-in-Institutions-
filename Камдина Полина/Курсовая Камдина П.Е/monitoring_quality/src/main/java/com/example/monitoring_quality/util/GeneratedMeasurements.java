package com.example.monitoring_quality.util;

public class GeneratedMeasurements {
    public static Double generateMeasurements(String params) {
        switch (params){
            case "temp":
                return (28 - 18) * Math.random() + 18;
            case "co2":
                return (1500 - 400) * Math.random() + 400;
            case "humidity":
                return (70 - 30) * Math.random() + 30;
            default:
                return null;
        }
    }
}
