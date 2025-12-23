package com.example.monitoring_quality.controller;

import com.example.monitoring_quality.model.Sensors;
import com.example.monitoring_quality.service.SensorsService;
import com.example.monitoring_quality.model.Rooms;
import com.example.monitoring_quality.service.RoomsService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;

import static com.example.monitoring_quality.util.HelpFullClass.*;

public class SensorsEditViewController {
    public Sensors sensors;
    public SensorsService modelNameService = new SensorsService();
    @FXML
    ComboBox<Rooms> idRooomCB;

    @FXML
    ComboBox<String> typeSensorCB;

    @FXML
    private TextField locationNoteTF;

    @FXML
    private Button saveBtn, cancelBtn;

    public void initForm(Sensors sensors, Stage mainStage) {
        this.sensors = sensors;
        saveBtn.setText((sensors != null) ? "Обновить" : "Добавить");
        if (sensors != null) initInputControllers();
        initComboBoxes();
    }

    @FXML
    private void onSaveBtn() {
        if (validValues()) {
            if (sensors != null) {
                setStateSensors();
                modelNameService.update(sensors);
            } else {
                sensors = new Sensors();
                setStateSensors();
                modelNameService.save(sensors);
            }
            saveBtn.getScene().getWindow().hide();
        }
    }

    @FXML
    private void onCancelBtn() {
        cancelBtn.getScene().getWindow().hide();
    }

    private void setStateSensors() {
        sensors.setRooms(idRooomCB.getValue());
        sensors.setTypeSensor(typeSensorCB.getValue());
        sensors.setLocationNote(locationNoteTF.getText());
    }

    private void initComboBoxes() {
        idRooomCB.getItems().addAll(new RoomsService().getAllRows());
        typeSensorCB.getItems().addAll("co2", "temp","humidity");
    }

    private void initInputControllers() {
        if (sensors.getRooms() != null) {
            idRooomCB.setValue(sensors.getRooms());
        }
        if (sensors.getTypeSensor() != null) {
            typeSensorCB.setValue(sensors.getTypeSensor());
        }
        if (sensors.getLocationNote() != null) {
            locationNoteTF.setText(sensors.getLocationNote());
        }
    }

    boolean validValues() {
        if (idRooomCB.getValue() == null) {
            showAlert("Room");
            return false;
        }
        if (typeSensorCB.getValue() == null || typeSensorCB.getValue().trim().isEmpty()) {
            showAlert("Type sensor");
            return false;
        }
        return true;
    }
}