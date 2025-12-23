package com.example.monitoring_quality.service;

import com.example.monitoring_quality.model.Sensors;
import com.example.monitoring_quality.util.HibernateSession;
import org.hibernate.Session;

import java.util.List;

public class SensorsService extends BaseService<Sensors> {
    public SensorsService() {
        super(Sensors.class);
    }

    public List<Sensors> getSensorsByRoomId(int roomId) {
        String querySQL = "from Sensors where rooms.idRooom = :roomId";
        try (Session session = HibernateSession.getSessionFactory().openSession()) {
            return session.createQuery(querySQL).setParameter("roomId", roomId).list();
        }
    }
}