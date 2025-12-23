package com.example.monitoring_quality.controller;

import com.example.monitoring_quality.model.Buildings;
import com.example.monitoring_quality.service.BuildingsService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;

import static com.example.monitoring_quality.util.HelpFullClass.*;

public class BuildingsEditViewController {
    public Buildings buildings;
    public BuildingsService modelNameService = new BuildingsService();
    @FXML
    TextField addressBuildingTF;

    @FXML
    TextField nameInstitutionTF;

    @FXML
    TextField abbreviationInstitutionTF;

    @FXML
    private Button saveBtn, cancelBtn;

    public void initForm(Buildings buildings, Stage mainStage) {
        this.buildings = buildings;
        saveBtn.setText((buildings != null) ? "Обновить" : "Добавить");
        if (buildings != null) initInputControllers();
        initComboBoxes();
    }

    @FXML
    private void onSaveBtn() {
        if (validValues()) {
            if (buildings != null) {
                setStateBuildings();
                modelNameService.update(buildings);
            } else {
                buildings = new Buildings();
                setStateBuildings();
                modelNameService.save(buildings);
            }
            saveBtn.getScene().getWindow().hide();
        }
    }

    @FXML
    private void onCancelBtn() {
        cancelBtn.getScene().getWindow().hide();
    }

    private void setStateBuildings() {
        buildings.setAddressBuilding(addressBuildingTF.getText());
        buildings.setNameInstitution(nameInstitutionTF.getText());
        buildings.setAbbreviationInstitution(abbreviationInstitutionTF.getText());
    }

    private void initComboBoxes() {
    }

    private void initInputControllers() {
        if (buildings.getAddressBuilding() != null) {
            addressBuildingTF.setText(buildings.getAddressBuilding());
        }
        if (buildings.getNameInstitution() != null) {
            nameInstitutionTF.setText(buildings.getNameInstitution());
        }
        if (buildings.getAbbreviationInstitution() != null) {
            abbreviationInstitutionTF.setText(buildings.getAbbreviationInstitution());
        }
    }

    boolean validValues() {
        if (addressBuildingTF.getText() == null || addressBuildingTF.getText().trim().isEmpty()) {
            showAlert("Address building");
            return false;
        }
        if (nameInstitutionTF.getText() == null || nameInstitutionTF.getText().trim().isEmpty()) {
            showAlert("Name institution");
            return false;
        }
        return true;
    }
}