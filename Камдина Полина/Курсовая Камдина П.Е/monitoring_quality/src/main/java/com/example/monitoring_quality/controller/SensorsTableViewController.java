package com.example.monitoring_quality.controller;

import com.example.monitoring_quality.MonitoringQualityApplication;
import com.example.monitoring_quality.model.*;
import com.example.monitoring_quality.service.SensorsService;
import javafx.beans.property.*;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.example.monitoring_quality.util.HelpFullClass.showModalStage;

public class SensorsTableViewController {
    @FXML
    private TableView<Sensors> tableViewSensors;
    @FXML
    private Button addBtn;
    @FXML
    private TableColumn<Sensors, String> tableColumnLocationNote;
    @FXML
    private TableColumn<Sensors, String> tableColumnTypeSensor;
    @FXML
    private TableColumn<Sensors, String> tableColumnIdSensor;
    @FXML
    private TableColumn<Sensors, String> tableColumnIdRooom;
    private final SensorsService sensorsService = new SensorsService();
    public Stage stage;

    @FXML
    private void initialize() {
        setCellValueFactories();
        tableViewRefresh();
    }

    private void setCellValueFactories() {
        tableColumnLocationNote.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%s", cellData.getValue().getLocationNote())));
        tableColumnTypeSensor.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%s", cellData.getValue().getTypeSensor())));
        tableColumnIdSensor.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%d", cellData.getValue().getIdSensor())));
        tableColumnIdRooom.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%s", cellData.getValue().getRooms())));
    }

    private void tableViewRefresh() {
        tableViewSensors.getItems().clear();
        tableViewSensors.getItems().addAll(sensorsService.getAllRows());
    }

    @FXML
    private void onAddBtn() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MonitoringQualityApplication.class.getResource("sensors-edit-view.fxml"));
        Parent parent = fxmlLoader.load();
        SensorsEditViewController controller = fxmlLoader.getController();
        controller.initForm(null, stage);
        showModalStage(stage, parent);
        tableViewRefresh();
    }

    @FXML
    private void onUpdateBtn() throws IOException {
        Sensors sensors = tableViewSensors.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader(MonitoringQualityApplication.class.getResource("sensors-edit-view.fxml"));
        Parent parent = fxmlLoader.load();
        SensorsEditViewController controller = fxmlLoader.getController();
        controller.initForm(sensors, stage);
        showModalStage(stage, parent);
        tableViewRefresh();
    }

    @FXML
    private void onDeleteBtn() {
        Sensors sensors = tableViewSensors.getSelectionModel().getSelectedItem();
        if (sensors != null) sensorsService.delete(sensors);
        tableViewRefresh();
    }

    @FXML
    public void MenuItemBuildingsAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(MonitoringQualityApplication.class.getResource("buildings-table-view.fxml"));
        Parent parent = loader.load();
        BuildingsTableViewController controller = loader.getController();
        controller.stage = stage;
        addBtn.getScene().setRoot(parent);
    }

    @FXML
    public void MenuItemRoomsAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(MonitoringQualityApplication.class.getResource("rooms-table-view.fxml"));
        Parent parent = loader.load();
        RoomsTableViewController controller = loader.getController();
        controller.stage = stage;
        addBtn.getScene().setRoot(parent);
    }

    @FXML
    public void MenuItemSensorsAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(MonitoringQualityApplication.class.getResource("sensors-table-view.fxml"));
        Parent parent = loader.load();
        SensorsTableViewController controller = loader.getController();
        controller.stage = stage;
        addBtn.getScene().setRoot(parent);
    }

    @FXML
    public void MenuItemMeasurementsAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(MonitoringQualityApplication.class.getResource("measurements-table-view.fxml"));
        Parent parent = loader.load();
        MeasurementsTableViewController controller = loader.getController();
        controller.stage = stage;
        addBtn.getScene().setRoot(parent);
    }

    @FXML
    public void MenuItemParamsAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(MonitoringQualityApplication.class.getResource("params-table-view.fxml"));
        Parent parent = loader.load();
        ParamsTableViewController controller = loader.getController();
        controller.stage = stage;
        addBtn.getScene().setRoot(parent);
    }

    @FXML
    public void MenuItemStandardsAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(MonitoringQualityApplication.class.getResource("standards-table-view.fxml"));
        Parent parent = loader.load();
        StandardsTableViewController controller = loader.getController();
        controller.stage = stage;
        addBtn.getScene().setRoot(parent);
    }

    @FXML
    public void MenuItemAlertsAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(MonitoringQualityApplication.class.getResource("alerts-table-view.fxml"));
        Parent parent = loader.load();
        AlertsTableViewController controller = loader.getController();
        controller.stage = stage;
        addBtn.getScene().setRoot(parent);
    }
}