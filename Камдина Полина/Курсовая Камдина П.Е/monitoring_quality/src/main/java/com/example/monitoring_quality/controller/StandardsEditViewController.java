package com.example.monitoring_quality.controller;

import com.example.monitoring_quality.model.Standards;
import com.example.monitoring_quality.service.StandardsService;
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

import static com.example.monitoring_quality.util.HelpFullClass.*;

public class StandardsEditViewController {
    public Standards standards;
    public StandardsService modelNameService = new StandardsService();
    @FXML
    private ComboBox<Rooms> idRooomCB;

    @FXML
    private ComboBox<Params> idParamCB;

    @FXML
    private TextField maxValueTF;

    @FXML
    private TextField minValueTF;

    @FXML
    private Button saveBtn, cancelBtn;

    public void initForm(Standards standards, Stage mainStage) {
        this.standards = standards;
        saveBtn.setText((standards != null) ? "Обновить" : "Добавить");
        if (standards != null) initInputControllers();
        initComboBoxes();
    }

    @FXML
    private void onSaveBtn() {
        if (validValues()) {
            if (standards != null) {
                setStateStandards();
                modelNameService.update(standards);
            } else {
                standards = new Standards();
                setStateStandards();
                modelNameService.save(standards);
            }
            saveBtn.getScene().getWindow().hide();
        }
    }

    @FXML
    private void onCancelBtn() {
        cancelBtn.getScene().getWindow().hide();
    }

    private void setStateStandards() {
        standards.setRooms(idRooomCB.getValue());
        standards.setParams(idParamCB.getValue());
        standards.setMaxValue(Double.parseDouble(maxValueTF.getText().replace(",", ".")));
        standards.setMinValue(Double.parseDouble(minValueTF.getText().replace(",", ".")));
    }

    private void initComboBoxes() {
        idRooomCB.getItems().addAll(new RoomsService().getAllRows());
        idParamCB.getItems().addAll(new ParamsService().getAllRows());
    }

    private void initInputControllers() {
        if (standards.getRooms() != null) {
            idRooomCB.setValue(standards.getRooms());
        }
        if (standards.getParams() != null) {
            idParamCB.setValue(standards.getParams());
        }
        if (standards.getMaxValue() != null) {
            maxValueTF.setText(String.format("%f", standards.getMaxValue()));
        }
        if (standards.getMinValue() != null) {
            minValueTF.setText(String.format("%f", standards.getMinValue()));
        }
    }

    private boolean validValues() {
        if (idRooomCB.getValue() == null) {
            showAlert("Room");
            return false;
        }
        if (idParamCB.getValue() == null) {
            showAlert("Param");
            return false;
        }
        if (maxValueTF.getText() == null || maxValueTF.getText().trim().isEmpty()) {
            showAlert("Max value");
            return false;
        }
        if (minValueTF.getText() == null || minValueTF.getText().trim().isEmpty()) {
            showAlert("Min value");
            return false;
        }
        return true;
    }
}