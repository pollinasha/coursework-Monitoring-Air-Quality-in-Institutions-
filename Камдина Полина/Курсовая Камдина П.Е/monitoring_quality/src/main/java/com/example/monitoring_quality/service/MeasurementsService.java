package com.example.monitoring_quality.service;

import com.example.monitoring_quality.model.DynamicMeasurementsDTO;
import com.example.monitoring_quality.model.Measurements;
import com.example.monitoring_quality.util.HibernateSession;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class MeasurementsService extends BaseService<Measurements> {
    public MeasurementsService() {
        super(Measurements.class);
    }

    public Double getAvgValueInSelectedDay(Integer idRoom, String sensorType, LocalDate selectedDate) {
        String querySQL = "select cast(avg(value) as float8) from measurements\n" +
                "join sensors ON measurements.id_sensor = sensors.id_sensor\n" +
                "JOIN rooms ON sensors.id_room = rooms.id_room\n" +
                "where sensors.id_room = :ROOM_ID and type_sensor = :SENSOR_TYPE and date(datetime) = date(:SELECTED_DATE)\n" +
                "group by number_room, type_sensor, date(datetime);";

        try (Session session = HibernateSession.getSessionFactory().openSession()) {
            return (Double) session.createNativeQuery(querySQL).setParameter("ROOM_ID", idRoom)
                    .setParameter("SENSOR_TYPE", sensorType)
                    .setParameter("SELECTED_DATE", selectedDate).uniqueResult();
        }
    }

    public List<DynamicMeasurementsDTO> getDataForGraphs(String roomNumber) {
        String querySQL = "select r.number_room, s.type_sensor, avg(m.value), date(m.datetime) " +
                "from measurements m " +
                "join sensors s on m.id_sensor = s.id_sensor " +
                "join rooms r on s.id_room = r.id_room " +
                "where r.number_room = :ROOM_NUMBER " +
                "group by date(m.datetime), r.number_room, s.type_sensor " +
                "order by date(m.datetime), r.number_room, s.type_sensor";

        try (Session session = HibernateSession.getSessionFactory().openSession()) {
            List<Object[]> results = session.createNativeQuery(querySQL, Object[].class)
                    .setParameter("ROOM_NUMBER", roomNumber)
                    .list();

            return results.stream()
                    .map(row -> {
                        DynamicMeasurementsDTO dto = new DynamicMeasurementsDTO();
                        dto.setNumberRoom((String) row[0]);
                        dto.setSensorType((String) row[1]);

                        // Преобразуем BigDecimal в Double если нужно
                        if (row[2] instanceof Number) {
                            dto.setMeasurementValue(((Number) row[2]).doubleValue());
                        } else if (row[2] != null) {
                            dto.setMeasurementValue(Double.valueOf(row[2].toString()));
                        }

                        // Преобразуем java.sql.Date в LocalDate
                        if (row[3] instanceof java.sql.Date) {
                            dto.setMeasurementDate(((java.sql.Date) row[3]).toLocalDate());
                        } else if (row[3] instanceof java.util.Date) {
                            dto.setMeasurementDate(((java.util.Date) row[3]).toInstant()
                                    .atZone(java.time.ZoneId.systemDefault()).toLocalDate());
                        } else if (row[3] instanceof LocalDate) {
                            dto.setMeasurementDate((LocalDate) row[3]);
                        }

                        return dto;
                    })
                    .collect(Collectors.toList());
        }
    }
}