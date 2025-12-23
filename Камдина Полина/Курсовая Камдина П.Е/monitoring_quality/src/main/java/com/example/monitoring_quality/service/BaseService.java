package com.example.monitoring_quality.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

import static com.example.monitoring_quality.util.HibernateSession.getSessionFactory;

public class BaseService<T> {
    private static final SessionFactory SESSION_FACTORY = getSessionFactory();
    private Class<T> model;

    public BaseService(Class<T> model) {
        this.model = model;
    }

    public List<T> getAllRows() {
        try (Session session = SESSION_FACTORY.openSession()) {
            return session.createQuery("from " + model.getSimpleName()).list();
        }
    }

    public T getOneRow(Long id) {
        try (Session session = SESSION_FACTORY.openSession()) {
            return session.get(model, id);
        }
    }

    public void save(T newRow) {
        Session session = SESSION_FACTORY.openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(newRow);
        transaction.commit();
    }

    public void update(T updateRow) {
        Session session = SESSION_FACTORY.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(updateRow);
        transaction.commit();
    }

    public void delete(T oldRow) {
        Session session = SESSION_FACTORY.openSession();
        Transaction transaction = session.beginTransaction();
        session.remove(oldRow);
        transaction.commit();
    }

    public Long getCountRows() {
        try (Session session = SESSION_FACTORY.openSession()) {
            return (Long) session.createQuery("select count(*) from " + model.getSimpleName()).uniqueResult();
        }
    }
}