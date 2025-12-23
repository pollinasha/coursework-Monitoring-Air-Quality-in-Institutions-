package com.example.monitoring_quality.controller;

import com.example.monitoring_quality.model.Alerts;
import com.example.monitoring_quality.service.AlertsService;
import com.example.monitoring_quality.model.Rooms;
import com.example.monitoring_quality.model.Params;
import com.example.monitoring_quality.service.RoomsService;
import com.example.monitoring_quality.service.ParamsService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.Date;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.example.monitoring_quality.util.HelpFullClass.*;

public class AlertsEditViewController {
    public Alerts alerts;
    public AlertsService modelNameService = new AlertsService();
    @FXML
    ComboBox<Rooms> idRooomCB;

    @FXML
    ComboBox<Params> idParamCB;

    @FXML
    TextField valueTF;

    @FXML
    TextField datetimeTF;

    @FXML
    CheckBox resolvedCB;

    @FXML
    TextField resolvedByTF;

    @FXML
    TextField resolutionTimeTF;

    @FXML
    Button saveBtn;
    @FXML
    Button cancelBtn;

    public void initForm(Alerts alerts, Stage mainStage) {
        this.alerts = alerts;
        saveBtn.setText((alerts != null) ? "Обновить" : "Добавить");
        if (alerts != null) initInputControllers();
        initComboBoxes();
    }

    @FXML
    void onSaveBtn() {
        if (validValues()) {
            if (alerts != null) {
                setStateAlerts();
                modelNameService.update(alerts);
            } else {
                alerts = new Alerts();
                setStateAlerts();
                modelNameService.save(alerts);
            }
            saveBtn.getScene().getWindow().hide();
        }
    }

    @FXML
    private void onCancelBtn() {
        cancelBtn.getScene().getWindow().hide();
    }

    void setStateAlerts() {
        alerts.setRooms(idRooomCB.getValue());
        alerts.setParams(idParamCB.getValue());
        alerts.setValue(Double.parseDouble(valueTF.getText().replace(",", ".")));
        alerts.setDatetime(LocalDateTime.parse(datetimeTF.getText()));
        alerts.setResolved(resolvedCB.isSelected());
        alerts.setResolvedBy(resolvedByTF.getText());
        alerts.setResolutionTime(LocalDateTime.parse(resolutionTimeTF.getText()));
    }

    private void initComboBoxes() {
        idRooomCB.getItems().addAll(new RoomsService().getAllRows());
        idParamCB.getItems().addAll(new ParamsService().getAllRows());
    }

    void initInputControllers() {
        if (alerts.getRooms() != null) {
            idRooomCB.setValue(alerts.getRooms());
        }
        if (alerts.getParams() != null) {
            idParamCB.setValue(alerts.getParams());
        }
        if (alerts.getValue() != null) {
            valueTF.setText(String.format("%f", alerts.getValue()));
        }
        if (alerts.getDatetime() != null) {
            datetimeTF.setText(String.format("%s", alerts.getDatetime()));
        }
        if (alerts.getResolved() != null) {
            resolvedCB.setSelected(alerts.getResolved());
        }
        if (alerts.getResolvedBy() != null) {
            resolvedByTF.setText(alerts.getResolvedBy());
        }
        if (alerts.getResolutionTime() != null) {
            resolutionTimeTF.setText(String.format("%s", alerts.getResolutionTime()));
        }
    }

    boolean validValues() {
        if (idRooomCB.getValue() == null) {
            showAlert("Rooms");
            return false;
        }
        if (idParamCB.getValue() == null) {
            showAlert("Param");
            return false;
        }
        if (valueTF.getText() == null || datetimeTF.getText().trim().isEmpty()) {
            showAlert("Value");
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
        if (resolvedByTF.getText() == null || resolvedByTF.getText().trim().isEmpty()) {
            showAlert("Resolved by");
            return false;
        }
        if (resolutionTimeTF.getText() == null || resolutionTimeTF.getText().trim().isEmpty()) {
            showAlert("time");
            return false;
        } else {
            try {
                LocalDateTime.parse(resolutionTimeTF.getText());
            } catch (DateTimeException e) {
                showAlert("Некорректное значение в Resolution time");
                return false;
            }
        }
        return true;
    }
}