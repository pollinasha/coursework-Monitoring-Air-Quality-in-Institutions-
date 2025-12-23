package com.example.monitoring_quality.model;

import java.time.*;

import jakarta.persistence.*;

@Entity
@Table(name = "alerts", schema = "public")
public class Alerts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alert")
    private Integer idAlert;
    @ManyToOne
    @JoinColumn(name = "id_room")
    private Rooms rooms;
    @ManyToOne
    @JoinColumn(name = "id_param")
    private Params params;
    @Column(name = "value")
    private Double value;
    @Column(name = "datetime")
    private LocalDateTime datetime;
    @Column(name = "resolved")
    private Boolean resolved;
    @Column(name = "resolved_by")
    private String resolvedBy;
    @Column(name = "resolution_time")
    private LocalDateTime resolutionTime;

    public Integer getIdAlert() {
        return idAlert;
    }

    public void setIdAlert(Integer idAlert) {
        this.idAlert = idAlert;
    }

    public Rooms getRooms() {
        return rooms;
    }

    public void setRooms(Rooms rooms) {
        this.rooms = rooms;
    }

    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public Boolean getResolved() {
        return resolved;
    }

    public void setResolved(Boolean resolved) {
        this.resolved = resolved;
    }

    public String getResolvedBy() {
        return resolvedBy;
    }

    public void setResolvedBy(String resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    public LocalDateTime getResolutionTime() {
        return resolutionTime;
    }

    public void setResolutionTime(LocalDateTime resolutionTime) {
        this.resolutionTime = resolutionTime;
    }
}