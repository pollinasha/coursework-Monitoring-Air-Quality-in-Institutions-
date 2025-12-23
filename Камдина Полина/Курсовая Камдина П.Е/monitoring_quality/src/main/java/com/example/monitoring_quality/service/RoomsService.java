package com.example.monitoring_quality.service;

import com.example.monitoring_quality.model.Rooms;
import com.example.monitoring_quality.util.HibernateSession;
import org.hibernate.Session;

import java.util.List;

public class RoomsService extends BaseService<Rooms> {
    public RoomsService() {
        super(Rooms.class);
    }

    public List<Rooms> getRoomsByBuildingId(int buildingId) {
        String querySQL = "from Rooms where buildings.idBuilding = :buildingId order by numberRoom";
        try (Session session = HibernateSession.getSessionFactory().openSession()) {
            return session.createQuery(querySQL).setParameter("buildingId", buildingId).list();
        }
    }
}