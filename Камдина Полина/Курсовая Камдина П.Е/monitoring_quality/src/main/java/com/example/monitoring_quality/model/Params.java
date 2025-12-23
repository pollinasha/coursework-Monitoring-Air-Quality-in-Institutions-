package com.example.monitoring_quality.model;

import jakarta.persistence.*;

@Entity
@Table(name = "params", schema = "public")
public class Params {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_param")
    private Integer idParam;
    @Column(name = "param")
    private String param;

    public Integer getIdParam() {
        return idParam;
    }

    public void setIdParam(Integer idParam) {
        this.idParam = idParam;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return param;
    }
}