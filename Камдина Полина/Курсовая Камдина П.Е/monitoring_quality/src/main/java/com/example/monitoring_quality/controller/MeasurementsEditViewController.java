package com.example.monitoring_quality.controller;

import com.example.monitoring_quality.model.Measurements;
import com.example.monitoring_quality.service.MeasurementsService;
import com.example.monitoring_quality.model.Sensors;
import com.example.monitoring_quality.service.SensorsService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.example.monitoring_quality.util.HelpFullClass.*;

public class MeasurementsEditViewController {
    public Measurements measurements;
    public MeasurementsService modelNameService = new MeasurementsService();
    @FXML
    ComboBox<Sensors> idSensorCB;

    @FXML
    TextField datetimeTF;

    @FXML
    TextField valueTF;

    @FXML
    private Button saveBtn, cancelBtn;

    public void initForm(Measurements measurements, Stage mainStage) {
        this.measurements = measurements;
        saveBtn.setText((measurements != null) ? "Обновить" : "Добавить");
        if (measurements != null) initInputControllers();
        initComboBoxes();
    }

    @FXML
    private void onSaveBtn() {
        if (validValues()) {
            if (measurements != null) {
                setStateMeasurements();
                modelNameService.update(measurements);
            } else {
                measurements = new Measurements();
                setStateMeasurements();
                modelNameService.save(measurements);
            }
            saveBtn.getScene().getWindow().hide();
        }
    }

    @FXML
    private void onCancelBtn() {
        cancelBtn.getScene().getWindow().hide();
    }

    private void setStateMeasurements() {
        measurements.setSensors(idSensorCB.getValue());
        measurements.setDatetime(LocalDateTime.parse(datetimeTF.getText()));
        measurements.setValue(Double.parseDouble(valueTF.getText().replace(",", ".")));
    }

    private void initComboBoxes() {
        idSensorCB.getItems().addAll(new SensorsService().getAllRows());
    }

    private void initInputControllers() {
        if (measurements.getSensors() != null) {
            idSensorCB.setValue(measurements.getSensors());
        }
        if (measurements.getDatetime() != null) {
            datetimeTF.setText(String.format("%s", measurements.getDatetime()));
        }
        if (measurements.getValue() != null) {
            valueTF.setText(String.format("%f", measurements.getValue()));
        }
    }

    boolean validValues() {
        if (idSensorCB.getValue() == null) {
            showAlert("Sensor");
            return false;
        }
        if (datetimeTF.getText() == null || datetimeTF.getText().trim().isEmpty()) {
            showAlert("Datetime");
            return false;
        } else {
            try {
                LocalDateTime.parse(datetimeTF.getText());
            } catch (DateTimeException e) {
                showAlert("Некорректное значение в Datetime");
                return false;
            }
        }
        if (valueTF.getText() == null || valueTF.getText().trim().isEmpty()) {
            showAlert("Values");
            return false;
        }
        return true;
    }
}