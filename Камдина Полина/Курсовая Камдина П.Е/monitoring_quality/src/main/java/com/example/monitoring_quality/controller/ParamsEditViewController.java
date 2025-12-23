package com.example.monitoring_quality.controller;

import com.example.monitoring_quality.model.Params;
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

public class ParamsEditViewController {
    public Params params;
    public ParamsService modelNameService = new ParamsService();
    @FXML
    TextField paramTF;

    @FXML
    private Button saveBtn, cancelBtn;

    public void initForm(Params params, Stage mainStage) {
        this.params = params;
        saveBtn.setText((params != null) ? "Обновить" : "Добавить");
        if (params != null) initInputControllers();
        initComboBoxes();
    }

    @FXML
    private void onSaveBtn() {
        if (validValues()) {
            if (params != null) {
                setStateParams();
                modelNameService.update(params);
            } else {
                params = new Params();
                setStateParams();
                modelNameService.save(params);
            }
            saveBtn.getScene().getWindow().hide();
        }
    }

    @FXML
    private void onCancelBtn() {
        cancelBtn.getScene().getWindow().hide();
    }

    private void setStateParams() {
        params.setParam(paramTF.getText());
    }

    private void initComboBoxes() {
    }

    private void initInputControllers() {
        if (params.getParam() != null) {
            paramTF.setText(params.getParam());
        }
    }

    boolean validValues() {
        if (paramTF.getText() == null || paramTF.getText().trim().isEmpty()) {
            showAlert("Param");
            return false;
        }
        return true;
    }
}