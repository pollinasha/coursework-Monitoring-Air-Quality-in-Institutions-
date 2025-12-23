package com.example.monitoring_quality.util;

import com.example.monitoring_quality.model.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class HibernateSession {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) try (FileInputStream fileInputStream = new FileInputStream("config.properties")) {
            Properties properties = new Properties();
            properties.load(fileInputStream);
            sessionFactory = new Configuration().setProperties(properties)
                    .addAnnotatedClass(Buildings.class)
                    .addAnnotatedClass(Rooms.class)
                    .addAnnotatedClass(Sensors.class)
                    .addAnnotatedClass(Measurements.class)
                    .addAnnotatedClass(Params.class)
                    .addAnnotatedClass(Standards.class)
                    .addAnnotatedClass(Alerts.class)
                    .buildSessionFactory();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return sessionFactory;
    }
}