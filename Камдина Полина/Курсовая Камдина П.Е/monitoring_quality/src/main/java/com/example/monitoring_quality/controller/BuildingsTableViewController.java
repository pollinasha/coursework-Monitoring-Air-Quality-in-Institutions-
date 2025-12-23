package com.example.monitoring_quality.controller;

import com.example.monitoring_quality.MonitoringQualityApplication;
import com.example.monitoring_quality.model.*;
import com.example.monitoring_quality.service.BuildingsService;
import javafx.beans.property.*;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.example.monitoring_quality.util.HelpFullClass.showAlert;
import static com.example.monitoring_quality.util.HelpFullClass.showModalStage;

public class BuildingsTableViewController {
    @FXML
    private TableView<Buildings> tableViewBuildings;
    @FXML
    private Button addBtn;
    @FXML
    private TableColumn<Buildings, String> tableColumnIdBuilding;
    @FXML
    private TableColumn<Buildings, String> tableColumnAddressBuilding;
    @FXML
    private TableColumn<Buildings, String> tableColumnAbbreviationInstitution;
    @FXML
    private TableColumn<Buildings, String> tableColumnNameInstitution;
    private final BuildingsService buildingsService = new BuildingsService();
    public Stage stage;

    @FXML
    private void initialize() {
        setCellValueFactories();
        tableViewRefresh();
    }

    private void setCellValueFactories() {
        tableColumnIdBuilding.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%d", cellData.getValue().getIdBuilding())));
        tableColumnAddressBuilding.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%s", cellData.getValue().getAddressBuilding())));
        tableColumnAbbreviationInstitution.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%s", cellData.getValue().getAbbreviationInstitution())));
        tableColumnNameInstitution.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%s", cellData.getValue().getNameInstitution())));
    }

    private void tableViewRefresh() {
        tableViewBuildings.getItems().clear();
        tableViewBuildings.getItems().addAll(buildingsService.getAllRows());
    }

    @FXML
    private void onAddBtn() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MonitoringQualityApplication.class.getResource("buildings-edit-view.fxml"));
        Parent parent = fxmlLoader.load();
        BuildingsEditViewController controller = fxmlLoader.getController();
        controller.initForm(null, stage);
        showModalStage(stage, parent);
        tableViewRefresh();
    }

    @FXML
    private void onUpdateBtn() throws IOException {
        Buildings buildings = tableViewBuildings.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader(MonitoringQualityApplication.class.getResource("buildings-edit-view.fxml"));
        Parent parent = fxmlLoader.load();
        BuildingsEditViewController controller = fxmlLoader.getController();
        controller.initForm(buildings, stage);
        showModalStage(stage, parent);
        tableViewRefresh();
    }

    @FXML
    private void onDeleteBtn() {
        Buildings buildings = tableViewBuildings.getSelectionModel().getSelectedItem();
        if (buildings != null) buildingsService.delete(buildings);
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
    public void onBuildingsState() throws IOException {
        Buildings buildings = tableViewBuildings.getSelectionModel().getSelectedItem();
        if (buildings != null) {
            FXMLLoader loader = new FXMLLoader(MonitoringQualityApplication.class.getResource("building-states-view.fxml"));
            Parent parent = loader.load();
            BuildingStatesViewController controller = loader.getController();
            controller.building = buildings;
            controller.stage = stage;
            controller.initializeData();
            addBtn.getScene().setRoot(parent);
        } else {
            showAlert("Выберите здание для вывода информации");
        }
    }
}