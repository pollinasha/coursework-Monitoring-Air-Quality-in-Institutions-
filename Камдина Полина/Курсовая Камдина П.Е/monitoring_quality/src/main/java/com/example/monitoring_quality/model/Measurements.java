package com.example.monitoring_quality.model;

import java.time.*;

import jakarta.persistence.*;

@Entity
@Table(name = "measurements", schema = "public")
public class Measurements {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_measurement")
    private Long idMeasurement;
    @ManyToOne
    @JoinColumn(name = "id_sensor")
    private Sensors sensors;
    @Column(name = "datetime")
    private LocalDateTime datetime;
    @Column(name = "value")
    private Double value;

    public Long getIdMeasurement() {
        return idMeasurement;
    }

    public void setIdMeasurement(Long idMeasurement) {
        this.idMeasurement = idMeasurement;
    }

    public Sensors getSensors() {
        return sensors;
    }

    public void setSensors(Sensors sensors) {
        this.sensors = sensors;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}