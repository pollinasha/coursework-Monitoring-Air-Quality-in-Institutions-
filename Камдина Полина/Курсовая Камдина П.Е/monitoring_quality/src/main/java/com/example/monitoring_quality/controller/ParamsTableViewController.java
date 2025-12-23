package com.example.monitoring_quality.controller;

import com.example.monitoring_quality.MonitoringQualityApplication;
import com.example.monitoring_quality.model.*;
import com.example.monitoring_quality.service.ParamsService;
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

public class ParamsTableViewController {
    @FXML
    private TableView<Params> tableViewParams;
    @FXML
    private Button addBtn;
    @FXML
    private TableColumn<Params, String> tableColumnParam;
    @FXML
    private TableColumn<Params, String> tableColumnIdParam;
    private final ParamsService paramsService = new ParamsService();
    public Stage stage;

    @FXML
    private void initialize() {
        setCellValueFactories();
        tableViewRefresh();
    }

    private void setCellValueFactories() {
        tableColumnParam.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%s", cellData.getValue().getParam())));
        tableColumnIdParam.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%d", cellData.getValue().getIdParam())));
    }

    private void tableViewRefresh() {
        tableViewParams.getItems().clear();
        tableViewParams.getItems().addAll(paramsService.getAllRows());
    }

    @FXML
    private void onAddBtn() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MonitoringQualityApplication.class.getResource("params-edit-view.fxml"));
        Parent parent = fxmlLoader.load();
        ParamsEditViewController controller = fxmlLoader.getController();
        controller.initForm(null, stage);
        showModalStage(stage, parent);
        tableViewRefresh();
    }

    @FXML
    private void onUpdateBtn() throws IOException {
        Params params = tableViewParams.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader(MonitoringQualityApplication.class.getResource("params-edit-view.fxml"));
        Parent parent = fxmlLoader.load();
        ParamsEditViewController controller = fxmlLoader.getController();
        controller.initForm(params, stage);
        showModalStage(stage, parent);
        tableViewRefresh();
    }

    @FXML
    private void onDeleteBtn() {
        Params params = tableViewParams.getSelectionModel().getSelectedItem();
        if (params != null) paramsService.delete(params);
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