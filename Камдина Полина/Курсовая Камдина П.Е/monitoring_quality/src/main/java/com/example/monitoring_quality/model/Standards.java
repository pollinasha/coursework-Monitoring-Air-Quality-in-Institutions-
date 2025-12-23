package com.example.monitoring_quality.model;

import jakarta.persistence.*;

@Entity
@Table(name = "standards", schema = "public")
public class Standards {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_standard")
    private Integer idStandard;
    @ManyToOne
    @JoinColumn(name = "id_room")
    private Rooms rooms;
    @ManyToOne
    @JoinColumn(name = "id_param")
    private Params params;
    @Column(name = "max_value")
    private Double maxValue;
    @Column(name = "min_value")
    private Double minValue;

    public Integer getIdStandard() {
        return idStandard;
    }

    public void setIdStandard(Integer idStandard) {
        this.idStandard = idStandard;
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

    public Double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }

    public Double getMinValue() {
        return minValue;
    }

    public void setMinValue(Double minValue) {
        this.minValue = minValue;
    }
}