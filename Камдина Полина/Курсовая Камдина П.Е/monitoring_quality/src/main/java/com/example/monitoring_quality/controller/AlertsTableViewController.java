package com.example.monitoring_quality.controller;

import com.example.monitoring_quality.MonitoringQualityApplication;
import com.example.monitoring_quality.model.*;
import com.example.monitoring_quality.service.AlertsService;
import javafx.beans.property.*;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.example.monitoring_quality.util.HelpFullClass.showAlert;
import static com.example.monitoring_quality.util.HelpFullClass.showModalStage;

public class AlertsTableViewController {
    @FXML
    private TableView<Alerts> tableViewAlerts;
    @FXML
    private Button addBtn;
    @FXML
    private TableColumn<Alerts, String> tableColumnDatetime;
    @FXML
    private TableColumn<Alerts, String> tableColumnResolvedBy;
    @FXML
    private TableColumn<Alerts, String> tableColumnIdAlert;
    @FXML
    private TableColumn<Alerts, String> tableColumnIdParam;
    @FXML
    private TableColumn<Alerts, String> tableColumnResolutionTime;
    @FXML
    private TableColumn<Alerts, String> tableColumnValue;
    @FXML
    private TableColumn<Alerts, String> tableColumnIdRooom;
    @FXML
    private TableColumn<Alerts, String> tableColumnResolved;
    private final AlertsService alertsService = new AlertsService();
    public Stage stage;

    @FXML
    private DatePicker slectedDatePicker;

    @FXML
    private void initialize() {
        setCellValueFactories();
        tableViewRefresh();
    }

    private void setCellValueFactories() {
        tableColumnDatetime.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDatetime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))));
        tableColumnResolvedBy.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%s", cellData.getValue().getResolvedBy())));
        tableColumnIdAlert.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%d", cellData.getValue().getIdAlert())));
        tableColumnIdParam.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%s", cellData.getValue().getParams())));
        tableColumnResolutionTime.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getResolutionTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))));
        tableColumnValue.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%.2f", cellData.getValue().getValue())));
        tableColumnIdRooom.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%s", cellData.getValue().getRooms())));
        tableColumnResolved.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%b", cellData.getValue().getResolved())));
    }

    private void tableViewRefresh() {
        tableViewAlerts.getItems().clear();
        tableViewAlerts.getItems().addAll(alertsService.getAllRows());
    }

    @FXML
    private void onAddBtn() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MonitoringQualityApplication.class.getResource("alerts-edit-view.fxml"));
        Parent parent = fxmlLoader.load();
        AlertsEditViewController controller = fxmlLoader.getController();
        controller.initForm(null, stage);
        showModalStage(stage, parent);
        tableViewRefresh();
    }

    @FXML
    private void onUpdateBtn() throws IOException {
        Alerts alerts = tableViewAlerts.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader(MonitoringQualityApplication.class.getResource("alerts-edit-view.fxml"));
        Parent parent = fxmlLoader.load();
        AlertsEditViewController controller = fxmlLoader.getController();
        controller.initForm(alerts, stage);
        showModalStage(stage, parent);
        tableViewRefresh();
    }

    @FXML
    private void onDeleteBtn() {
        Alerts alerts = tableViewAlerts.getSelectionModel().getSelectedItem();
        if (alerts != null) alertsService.delete(alerts);
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
    public void onGetReportAlerts() throws IOException {
        LocalDate selectedDate = slectedDatePicker.getValue();
        if(selectedDate != null) {
            try (XSSFWorkbook file = new XSSFWorkbook()) {
                XSSFSheet dataSheet = file.createSheet("Исходные данные");
                List<Alerts> alerts = new AlertsService().getAllRows();
                createReportAlertsData(dataSheet, alerts.stream().filter(alert ->
                        alert.getDatetime().toLocalDate().isEqual(selectedDate)).toList());
                saveReportWithPath(file, selectedDate);
            }
        } else {
            showAlert("Ошибка", "Выберите дату для отчёта", true);
        }
    }

    public static void saveReportWithPath(XSSFWorkbook workbook,  LocalDate selectedDate) {
        try {
            Path desktop = Paths.get(System.getProperty("user.home"), "Desktop");
            Path reportsFolder = desktop.resolve("Отчёты об превышениях нормы");

            if (!Files.exists(reportsFolder)) {
                Files.createDirectory(reportsFolder);
                System.out.println("Создана папка: " + reportsFolder);
            }

            String fileName = String.format("report_alerts_%s.xlsx", selectedDate.format(DateTimeFormatter.ofPattern("dd_MM_yyyy")));
            Path reportFile = reportsFolder.resolve(fileName);

            try (FileOutputStream fos = new FileOutputStream(reportFile.toFile())) {
                workbook.write(fos);
            }

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }

    public static void createReportAlertsData(XSSFSheet sheet, List<Alerts> data) {
        XSSFRow header = sheet.createRow(0);
        header.createCell(0).setCellValue("№");
        header.createCell(1).setCellValue("Комната");
        header.createCell(2).setCellValue("Параметр");
        header.createCell(3).setCellValue("Значение");
        header.createCell(4).setCellValue("Дата и время");
        header.createCell(5).setCellValue("Решён?");
        header.createCell(6).setCellValue("Кем решено");
        header.createCell(7).setCellValue("Дата решения");

        for (int i = 0; i < data.size(); i++) {
            XSSFRow row = sheet.createRow(i + 1);
            Alerts alert = data.get(i);
            row.createCell(0).setCellValue(alert.getIdAlert());
            row.createCell(1).setCellValue(String.format("%s", alert.getRooms()));
            row.createCell(2).setCellValue(String.format("%s", alert.getParams()));
            row.createCell(3).setCellValue(alert.getValue());
            row.createCell(4).setCellValue(alert.getDatetime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
            row.createCell(5).setCellValue(alert.getResolved());
            row.createCell(6).setCellValue(alert.getResolvedBy());
            row.createCell(7).setCellValue(alert.getResolutionTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        }

        for (int i = 0; i < 7; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}