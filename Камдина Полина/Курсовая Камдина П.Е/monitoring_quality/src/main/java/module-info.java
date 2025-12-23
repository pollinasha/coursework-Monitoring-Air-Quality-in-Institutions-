module com.example.monitoring_quality {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires org.postgresql.jdbc;
    requires javafx.base;
    requires java.naming;
    requires java.desktop;
    requires javafx.graphics;
    requires org.apache.poi.ooxml;
    opens com.example.monitoring_quality to javafx.fxml;
    opens com.example.monitoring_quality.controller to javafx.fxml;
    opens com.example.monitoring_quality.model to org.hibernate.orm.core, javafx.base, com.fasterxml.jackson.databind;
    exports com.example.monitoring_quality;
    exports com.example.monitoring_quality.controller;
    exports com.example.monitoring_quality.model;
    exports com.example.monitoring_quality.service;
    exports com.example.monitoring_quality.util;
}