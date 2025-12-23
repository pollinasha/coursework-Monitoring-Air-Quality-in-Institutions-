package com.example.monitoring_quality.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "buildings", schema = "public")
public class Buildings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_building")
    private Integer idBuilding;
    @Column(name = "address_building")
    private String addressBuilding;
    @Column(name = "name_institution")
    private String nameInstitution;
    @Column(name = "abbreviation_institution")
    private String abbreviationInstitution;
    @OneToMany(mappedBy = "buildings")
    private Set<Rooms> rooms = new HashSet<>();

    public Integer getIdBuilding() {
        return idBuilding;
    }

    public void setIdBuilding(Integer idBuilding) {
        this.idBuilding = idBuilding;
    }

    public String getAddressBuilding() {
        return addressBuilding;
    }

    public void setAddressBuilding(String addressBuilding) {
        this.addressBuilding = addressBuilding;
    }

    public String getNameInstitution() {
        return nameInstitution;
    }

    public void setNameInstitution(String nameInstitution) {
        this.nameInstitution = nameInstitution;
    }

    public String getAbbreviationInstitution() {
        return abbreviationInstitution;
    }

    public void setAbbreviationInstitution(String abbreviationInstitution) {
        this.abbreviationInstitution = abbreviationInstitution;
    }

    @Override
    public String toString() {
        return nameInstitution + " " + addressBuilding;
    }

    public Set<Rooms> getRooms() {
        return rooms;
    }

    public void setRooms(Set<Rooms> rooms) {
        this.rooms = rooms;
    }
}