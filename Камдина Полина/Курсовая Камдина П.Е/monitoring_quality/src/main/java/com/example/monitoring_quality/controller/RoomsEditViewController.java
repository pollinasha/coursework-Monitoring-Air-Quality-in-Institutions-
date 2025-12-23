package com.example.monitoring_quality.controller;

import com.example.monitoring_quality.model.Rooms;
import com.example.monitoring_quality.service.RoomsService;
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

public class RoomsEditViewController {
    public Rooms rooms;
    public RoomsService modelNameService = new RoomsService();
    @FXML
    TextField numberRoomTF;

    @FXML
    ComboBox<String> typeRoomCB;

    @FXML
    ComboBox<Buildings> idBuildingCB;

    @FXML
    private Button saveBtn, cancelBtn;

    public void initForm(Rooms rooms, Stage mainStage) {
        this.rooms = rooms;
        saveBtn.setText((rooms != null) ? "Обновить" : "Добавить");
        if (rooms != null) initInputControllers();
        initComboBoxes();
    }

    @FXML
    private void onSaveBtn() {
        if (validValues()) {
            if (rooms != null) {
                setStateRooms();
                modelNameService.update(rooms);
            } else {
                rooms = new Rooms();
                setStateRooms();
                modelNameService.save(rooms);
            }
            saveBtn.getScene().getWindow().hide();
        }
    }

    @FXML
    private void onCancelBtn() {
        cancelBtn.getScene().getWindow().hide();
    }

    private void setStateRooms() {
        rooms.setNumberRoom(numberRoomTF.getText());
        rooms.setTypeRoom(typeRoomCB.getValue());
        rooms.setBuildings(idBuildingCB.getValue());
    }

    private void initComboBoxes() {
        idBuildingCB.getItems().addAll(new BuildingsService().getAllRows());
        typeRoomCB.getItems().addAll("кабинет","актовый","столовая");
    }

    private void initInputControllers() {
        if (rooms.getNumberRoom() != null) {
            numberRoomTF.setText(rooms.getNumberRoom());
        }
        if (rooms.getTypeRoom() != null) {
            typeRoomCB.setValue(rooms.getTypeRoom());
        }
        if (rooms.getBuildings() != null) {
            idBuildingCB.setValue(rooms.getBuildings());
        }
    }

    boolean validValues() {
        if (numberRoomTF.getText() == null || numberRoomTF.getText().trim().isEmpty()) {
            showAlert("Number room");
            return false;
        }
        if (typeRoomCB.getValue() == null || typeRoomCB.getValue().trim().isEmpty()) {
            showAlert("Type room");
            return false;
        }
        if (idBuildingCB.getValue() == null) {
            showAlert("Building");
            return false;
        }
        return true;
    }
}