package com.example.monitoring_quality.controller;

import com.example.monitoring_quality.MonitoringQualityApplication;
import com.example.monitoring_quality.model.*;
import com.example.monitoring_quality.service.StandardsService;
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

public class StandardsTableViewController {
    @FXML
    private TableView<Standards> tableViewStandards;
    @FXML
    private Button addBtn;
    @FXML
    private TableColumn<Standards, String> tableColumnMinValue;
    @FXML
    private TableColumn<Standards, String> tableColumnMaxValue;
    @FXML
    private TableColumn<Standards, String> tableColumnIdStandard;
    @FXML
    private TableColumn<Standards, String> tableColumnIdParam;
    @FXML
    private TableColumn<Standards, String> tableColumnIdRooom;
    private final StandardsService standardsService = new StandardsService();
    public Stage stage;

    @FXML
    private void initialize() {
        setCellValueFactories();
        tableViewRefresh();
    }

    private void setCellValueFactories() {
        tableColumnMinValue.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%.2f", cellData.getValue().getMinValue())));
        tableColumnMaxValue.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%.2f", cellData.getValue().getMaxValue())));
        tableColumnIdStandard.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%d", cellData.getValue().getIdStandard())));
        tableColumnIdParam.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%s", cellData.getValue().getParams())));
        tableColumnIdRooom.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%s", cellData.getValue().getRooms())));
    }

    private void tableViewRefresh() {
        tableViewStandards.getItems().clear();
        tableViewStandards.getItems().addAll(standardsService.getAllRows());
    }

    @FXML
    private void onAddBtn() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MonitoringQualityApplication.class.getResource("standards-edit-view.fxml"));
        Parent parent = fxmlLoader.load();
        StandardsEditViewController controller = fxmlLoader.getController();
        controller.initForm(null, stage);
        showModalStage(stage, parent);
        tableViewRefresh();
    }

    @FXML
    private void onUpdateBtn() throws IOException {
        Standards standards = tableViewStandards.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader(MonitoringQualityApplication.class.getResource("standards-edit-view.fxml"));
        Parent parent = fxmlLoader.load();
        StandardsEditViewController controller = fxmlLoader.getController();
        controller.initForm(standards, stage);
        showModalStage(stage, parent);
        tableViewRefresh();
    }

    @FXML
    private void onDeleteBtn() {
        Standards standards = tableViewStandards.getSelectionModel().getSelectedItem();
        if (standards != null) standardsService.delete(standards);
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