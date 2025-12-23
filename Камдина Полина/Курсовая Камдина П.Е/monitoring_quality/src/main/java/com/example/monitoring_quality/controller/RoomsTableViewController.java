package com.example.monitoring_quality.controller;

import com.example.monitoring_quality.MonitoringQualityApplication;
import com.example.monitoring_quality.model.*;
import com.example.monitoring_quality.service.RoomsService;
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

public class RoomsTableViewController {
    @FXML
    private TableView<Rooms> tableViewRooms;
    @FXML
    private Button addBtn;
    @FXML
    private TableColumn<Rooms, String> tableColumnIdBuilding;
    @FXML
    private TableColumn<Rooms, String> tableColumnTypeRoom;
    @FXML
    private TableColumn<Rooms, String> tableColumnNumberRoom;
    @FXML
    private TableColumn<Rooms, String> tableColumnIdRooom;
    private final RoomsService roomsService = new RoomsService();
    public Stage stage;

    @FXML
    private void initialize() {
        setCellValueFactories();
        tableViewRefresh();
    }

    private void setCellValueFactories() {
        tableColumnIdBuilding.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%s", cellData.getValue().getBuildings())));
        tableColumnTypeRoom.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%s", cellData.getValue().getTypeRoom())));
        tableColumnNumberRoom.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%s", cellData.getValue().getNumberRoom())));
        tableColumnIdRooom.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%d", cellData.getValue().getIdRooom())));
    }

    private void tableViewRefresh() {
        tableViewRooms.getItems().clear();
        tableViewRooms.getItems().addAll(roomsService.getAllRows());
    }

    @FXML
    private void onAddBtn() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MonitoringQualityApplication.class.getResource("rooms-edit-view.fxml"));
        Parent parent = fxmlLoader.load();
        RoomsEditViewController controller = fxmlLoader.getController();
        controller.initForm(null, stage);
        showModalStage(stage, parent);
        tableViewRefresh();
    }

    @FXML
    private void onUpdateBtn() throws IOException {
        Rooms rooms = tableViewRooms.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader(MonitoringQualityApplication.class.getResource("rooms-edit-view.fxml"));
        Parent parent = fxmlLoader.load();
        RoomsEditViewController controller = fxmlLoader.getController();
        controller.initForm(rooms, stage);
        showModalStage(stage, parent);
        tableViewRefresh();
    }

    @FXML
    private void onDeleteBtn() {
        Rooms rooms = tableViewRooms.getSelectionModel().getSelectedItem();
        if (rooms != null) roomsService.delete(rooms);
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