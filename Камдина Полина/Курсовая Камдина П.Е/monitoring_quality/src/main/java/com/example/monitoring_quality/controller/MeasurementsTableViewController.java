package com.example.monitoring_quality.controller;

import com.example.monitoring_quality.MonitoringQualityApplication;
import com.example.monitoring_quality.model.*;
import com.example.monitoring_quality.service.MeasurementsService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.example.monitoring_quality.util.HelpFullClass.showModalStage;

public class MeasurementsTableViewController {
    @FXML
    private TableView<Measurements> tableViewMeasurements;
    @FXML
    private Button addBtn;
    @FXML
    private TableColumn<Measurements, String> tableColumnDatetime;
    @FXML
    private TableColumn<Measurements, String> tableColumnIdSensor;
    @FXML
    private TableColumn<Measurements, String> tableColumnValue;
    @FXML
    private TableColumn<Measurements, String> tableColumnIdMeasurement;
    private final MeasurementsService measurementsService = new MeasurementsService();
    public Stage stage;

    @FXML
    private void initialize() {
        setCellValueFactories();
        tableViewRefresh();
    }

    private void setCellValueFactories() {
        tableColumnDatetime.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDatetime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))));
        tableColumnIdSensor.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%s", cellData.getValue().getSensors())));
        tableColumnValue.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%.2f", cellData.getValue().getValue())));
        tableColumnIdMeasurement.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%d", cellData.getValue().getIdMeasurement())));
    }

    private void tableViewRefresh() {
        tableViewMeasurements.getItems().clear();
        tableViewMeasurements.getItems().addAll(measurementsService.getAllRows());
    }

    @FXML
    private void onAddBtn() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MonitoringQualityApplication.class.getResource("measurements-edit-view.fxml"));
        Parent parent = fxmlLoader.load();
        MeasurementsEditViewController controller = fxmlLoader.getController();
        controller.initForm(null, stage);
        showModalStage(stage, parent);
        tableViewRefresh();
    }

    @FXML
    private void onUpdateBtn() throws IOException {
        Measurements measurements = tableViewMeasurements.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader(MonitoringQualityApplication.class.getResource("measurements-edit-view.fxml"));
        Parent parent = fxmlLoader.load();
        MeasurementsEditViewController controller = fxmlLoader.getController();
        controller.initForm(measurements, stage);
        showModalStage(stage, parent);
        tableViewRefresh();
    }

    @FXML
    private void onDeleteBtn() {
        Measurements measurements = tableViewMeasurements.getSelectionModel().getSelectedItem();
        if (measurements != null) measurementsService.delete(measurements);
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

    @FXML
    public void onGenerateMeasurements() {
        List<Sensors> allSensors = new SensorsService().getAllRows();
        Random random = new Random();
        Sensors generateSensors = new SensorsService().getOneRow((long) random.nextInt(allSensors.size() + 1));

        Measurements measurements = new Measurements();
        measurements.setSensors(generateSensors);
        measurements.setDatetime(LocalDateTime.now());
        measurements.setValue(generateMeasurements(generateSensors.getTypeSensor()));

        new MeasurementsService().save(measurements);
        tableViewRefresh();
    }

    public static Double generateMeasurements(String params) {
        switch (params){
            case "temp":
                return (28 - 18) * Math.random() + 18;
            case "co2":
                return (1500 - 400) * Math.random() + 400;
            case "humidity":
                return (70 - 30) * Math.random() + 30;
            default:
                return null;
        }
    }
}
