package com.example.monitoring_quality.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "rooms", schema = "public")
public class Rooms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_room")
    private Integer idRooom;
    @Column(name = "number_room")
    private String numberRoom;
    @Column(name = "type_room")
    private String typeRoom;
    @ManyToOne
    @JoinColumn(name = "id_building")
    private Buildings buildings;

    public Integer getIdRooom() {
        return idRooom;
    }

    public void setIdRooom(Integer idRooom) {
        this.idRooom = idRooom;
    }

    public String getNumberRoom() {
        return numberRoom;
    }

    public void setNumberRoom(String numberRoom) {
        this.numberRoom = numberRoom;
    }

    public String getTypeRoom() {
        return typeRoom;
    }

    public void setTypeRoom(String typeRoom) {
        this.typeRoom = typeRoom;
    }

    public Buildings getBuildings() {
        return buildings;
    }

    public void setBuildings(Buildings buildings) {
        this.buildings = buildings;
    }

    @Override
    public String toString() {
        return numberRoom + " " + typeRoom;
    }
}