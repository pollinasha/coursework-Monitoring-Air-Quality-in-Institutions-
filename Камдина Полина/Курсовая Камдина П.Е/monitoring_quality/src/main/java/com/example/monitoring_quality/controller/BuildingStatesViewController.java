package com.example.monitoring_quality.controller;

import com.example.monitoring_quality.MonitoringQualityApplication;
import com.example.monitoring_quality.model.RoomStatusDTO;
import com.example.monitoring_quality.model.*;
import com.example.monitoring_quality.service.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.PresetColor;
import org.apache.poi.xddf.usermodel.XDDFLineProperties;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.monitoring_quality.util.HelpFullClass.showAlert;

public class BuildingStatesViewController {
    public Buildings building;
    public Stage stage;

    @FXML
    private TableView<RoomStatusDTO> buildingSatesTableView;

    @FXML
    private Button backBtn;

    @FXML
    private DatePicker selectedDateDP;

    @FXML
    private TableColumn<RoomStatusDTO, String> tableColumnRoom;

    @FXML
    private TableColumn<RoomStatusDTO, String> tableColumnCO2;

    @FXML
    private TableColumn<RoomStatusDTO, String> tableColumnTemp;

    @FXML
    private TableColumn<RoomStatusDTO, String> tableColumnHumidity;

    @FXML
    private TableColumn<RoomStatusDTO, String> tableColumnStatus;

    @FXML
    private Label buildingNameLabel;

    private RoomsService roomsService = new RoomsService();
    private MeasurementsService measurementsService = new MeasurementsService();

    public void initializeData() {
        if (building == null) {
            showAlert("Ошибка", "Здание не выбрано",true);
            return;
        }
        buildingNameLabel.setText(buildingNameLabel.getText() + building);
        selectedDateDP.setValue(LocalDate.of(2025, 12, 22));
        setCellValueFactories();
        datePickerAction();
    }

    public void setCellValueFactories() {
        tableColumnRoom.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRoomNumber()));
        tableColumnCO2.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFormattedCo2()));
        tableColumnTemp.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFormattedTemperature()));
        tableColumnHumidity.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFormattedHumidity()));
        tableColumnStatus.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getOverallStatus()));
    }

    @FXML
    private void onBackBtn() throws IOException {
        FXMLLoader loader = new FXMLLoader(MonitoringQualityApplication.class.getResource("buildings-table-view.fxml"));
        Parent parent = loader.load();
        BuildingsTableViewController controller = loader.getController();
        controller.stage = stage;
        backBtn.getScene().setRoot(parent);
    }

    @FXML
    private void datePickerAction() {
        buildingSatesTableView.getItems().clear();
        List<Rooms> roomsInBuilding = roomsService.getRoomsByBuildingId(building.getIdBuilding());
        for (Rooms room : roomsInBuilding) {
            buildingSatesTableView.getItems().add(createRoomStatusDTO(room));
        }
    }

    public RoomStatusDTO createRoomStatusDTO(Rooms room) {
        RoomStatusDTO result = new RoomStatusDTO();
        result.setRoomNumber(room.getNumberRoom());
        result.setCo2Value(measurementsService.getAvgValueInSelectedDay(room.getIdRooom(), "co2", selectedDateDP.getValue()));
        result.setTemperature(measurementsService.getAvgValueInSelectedDay(room.getIdRooom(), "temp", selectedDateDP.getValue()));
        result.setHumidity(measurementsService.getAvgValueInSelectedDay(room.getIdRooom(), "humidity", selectedDateDP.getValue()));
        return result;
    }

    @FXML
    public void getDynamicsGraph() {
        // Проверяем, выбрана ли комната
        RoomStatusDTO selectedRoom = buildingSatesTableView.getSelectionModel().getSelectedItem();
        if (selectedRoom == null) {
            showAlert("Комнату", "Пожалуйста, выберите комнату из таблицы", false);
            return;
        }

        String roomNumber = selectedRoom.getRoomNumber();
        List<DynamicMeasurementsDTO> data = measurementsService.getDataForGraphs(roomNumber);

        if (data.isEmpty()) {
            showAlert("Информация", "Нет данных для выбранной комнаты: " + roomNumber, true);
            return;
        }

        try (XSSFWorkbook file = new XSSFWorkbook()) {
            XSSFSheet dataSheet = file.createSheet("Исходные данные");
            createBuildingStatesData(dataSheet, data);

            createSeparateChartsPerSensor(file, data);

            saveReportWithPath(file, roomNumber);

        } catch (Exception e) {
            showAlert("Ошибка", "Не удалось создать графики: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    public static void saveReportWithPath(XSSFWorkbook workbook,  String numberRoom) {
        try {
            Path desktop = Paths.get(System.getProperty("user.home"), "Desktop");
            Path reportsFolder = desktop.resolve("Графики динамики состояния здания");

            if (!Files.exists(reportsFolder)) {
                Files.createDirectory(reportsFolder);
                System.out.println("Создана папка: " + reportsFolder);
            }

            String fileName = String.format("analytics_state_%s.xlsx", numberRoom);
            Path reportFile = reportsFolder.resolve(fileName);

            try (FileOutputStream fos = new FileOutputStream(reportFile.toFile())) {
                workbook.write(fos);
            }

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }

    public static void createBuildingStatesData(XSSFSheet sheet, List<DynamicMeasurementsDTO> data) {
        XSSFRow header = sheet.createRow(0);
        header.createCell(0).setCellValue("Комната");
        header.createCell(1).setCellValue("Тип сенсора");
        header.createCell(2).setCellValue("Значение");
        header.createCell(3).setCellValue("Дата");

        for (int i = 0; i < data.size(); i++) {
            XSSFRow row = sheet.createRow(i + 1);
            DynamicMeasurementsDTO measurementsDTO = data.get(i);
            row.createCell(0).setCellValue(measurementsDTO.getNumberRoom());
            row.createCell(1).setCellValue(measurementsDTO.getSensorType());
            row.createCell(2).setCellValue(measurementsDTO.getMeasurementValue());

            if (measurementsDTO.getMeasurementDate() != null) {
                row.createCell(3).setCellValue(measurementsDTO.getFormattedDateTime());
            }
        }

        for (int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    public static void createSeparateChartsPerSensor(XSSFWorkbook workbook, List<DynamicMeasurementsDTO> data) {
        if (data.isEmpty()) {
            XSSFSheet infoSheet = workbook.createSheet("Информация");
            XSSFRow row = infoSheet.createRow(0);
            row.createCell(0).setCellValue("Нет данных для построения графиков");
            return;
        }

        // Получаем номер комнаты из данных
        String roomNumber = data.get(0).getNumberRoom();

        // Группируем данные по типу сенсора
        Map<String, List<DynamicMeasurementsDTO>> dataBySensor = data.stream()
                .collect(Collectors.groupingBy(DynamicMeasurementsDTO::getSensorType));

        int chartCount = 0;

        // Сначала создаем сводный лист с данными
        XSSFSheet summarySheet = workbook.createSheet("Сводные данные");
        createSummaryDataSheet(summarySheet, data, roomNumber);

        // Создаем графики для каждого типа сенсора
        for (Map.Entry<String, List<DynamicMeasurementsDTO>> sensorEntry : dataBySensor.entrySet()) {
            String sensorType = sensorEntry.getKey();
            List<DynamicMeasurementsDTO> sensorData = sensorEntry.getValue();

            // Сортируем по дате
            sensorData.sort(Comparator.comparing(DynamicMeasurementsDTO::getMeasurementDate));

            // Создаем лист для данных этого сенсора
            XSSFSheet dataSheet = workbook.createSheet("Данные " + getSensorDisplayName(sensorType));
            createSensorDataSheet(dataSheet, sensorData, sensorType, roomNumber);

            // Создаем лист для графика
            XSSFSheet chartSheet = workbook.createSheet("График " + getSensorDisplayName(sensorType));
            createSingleSensorChart(dataSheet, chartSheet, sensorData, sensorType, chartCount, roomNumber);

            chartCount++;
        }
    }

    private static void createSummaryDataSheet(XSSFSheet sheet, List<DynamicMeasurementsDTO> data, String roomNumber) {
        // Группируем по типу сенсора и дате
        Map<String, Map<LocalDate, Double>> summaryData = new TreeMap<>();

        for (DynamicMeasurementsDTO dto : data) {
            summaryData
                    .computeIfAbsent(dto.getSensorType(), k -> new TreeMap<>())
                    .put(dto.getMeasurementDate(), dto.getMeasurementValue());
        }

        // Заголовок
        XSSFRow headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Дата");

        int col = 1;
        List<String> sensorTypes = new ArrayList<>(summaryData.keySet());
        for (String sensorType : sensorTypes) {
            headerRow.createCell(col++).setCellValue(getSensorDisplayName(sensorType));
        }

        // Собираем все уникальные даты
        Set<LocalDate> allDates = new TreeSet<>();
        for (Map<LocalDate, Double> dateMap : summaryData.values()) {
            allDates.addAll(dateMap.keySet());
        }

        // Заполняем данные
        int rowNum = 1;
        for (LocalDate date : allDates) {
            XSSFRow row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

            col = 1;
            for (String sensorType : sensorTypes) {
                Double value = summaryData.get(sensorType).get(date);
                if (value != null) {
                    row.createCell(col).setCellValue(value);
                }
                col++;
            }
        }

        // Автоширина колонок
        for (int i = 0; i <= sensorTypes.size(); i++) {
            sheet.autoSizeColumn(i);
        }

        // Добавляем примечание
        XSSFRow noteRow = sheet.createRow(rowNum + 1);
        noteRow.createCell(0).setCellValue("Комната: " + roomNumber);
    }

    private static void createSensorDataSheet(XSSFSheet sheet, List<DynamicMeasurementsDTO> data,
                                              String sensorType, String roomNumber) {
        // Сортируем по дате
        data.sort(Comparator.comparing(DynamicMeasurementsDTO::getMeasurementDate));

        XSSFRow header = sheet.createRow(0);
        header.createCell(0).setCellValue("Комната");
        header.createCell(1).setCellValue("Дата");
        header.createCell(2).setCellValue(getSensorDisplayName(sensorType));

        int rowIndex = 1;
        for (DynamicMeasurementsDTO dto : data) {
            XSSFRow row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(roomNumber);

            if (dto.getMeasurementDate() != null) {
                row.createCell(1).setCellValue(dto.getFormattedDateTime());
            }

            row.createCell(2).setCellValue(dto.getMeasurementValue());
        }

        for (int i = 0; i < 3; i++) {
            sheet.autoSizeColumn(i);
        }

        // Добавляем статистику
        XSSFRow statsRow = sheet.createRow(rowIndex + 1);
        statsRow.createCell(0).setCellValue("Статистика:");

        XSSFRow avgRow = sheet.createRow(rowIndex + 2);
        avgRow.createCell(0).setCellValue("Среднее:");
        double average = data.stream()
                .mapToDouble(DynamicMeasurementsDTO::getMeasurementValue)
                .average()
                .orElse(0.0);
        avgRow.createCell(1).setCellValue(average);

        XSSFRow minRow = sheet.createRow(rowIndex + 3);
        minRow.createCell(0).setCellValue("Минимум:");
        double min = data.stream()
                .mapToDouble(DynamicMeasurementsDTO::getMeasurementValue)
                .min()
                .orElse(0.0);
        minRow.createCell(1).setCellValue(min);

        XSSFRow maxRow = sheet.createRow(rowIndex + 4);
        maxRow.createCell(0).setCellValue("Максимум:");
        double max = data.stream()
                .mapToDouble(DynamicMeasurementsDTO::getMeasurementValue)
                .max()
                .orElse(0.0);
        maxRow.createCell(1).setCellValue(max);
    }

    private static void createSingleSensorChart(XSSFSheet dataSheet, XSSFSheet chartSheet,
                                                List<DynamicMeasurementsDTO> data, String sensorType,
                                                int chartIndex, String roomNumber) {
        if (data.size() < 2) {
            XSSFRow row = chartSheet.createRow(0);
            row.createCell(0).setCellValue("Недостаточно данных для построения графика");
            return;
        }

        // Сортируем данные по дате
        data.sort(Comparator.comparing(DynamicMeasurementsDTO::getMeasurementDate));

        XSSFDrawing drawing = chartSheet.createDrawingPatriarch();

        // Позиционируем график
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, 0, 15, 25);

        XSSFChart chart = drawing.createChart(anchor);

        // Оси
        XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        bottomAxis.setTitle("Дата");

        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
        leftAxis.setTitle(getSensorDisplayName(sensorType));

        // Данные для линейного графика
        XDDFChartData chartData = chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);

        // Создаем серию данных
        int firstRow = 1;
        int lastRow = data.size();

        XDDFDataSource<String> dates = XDDFDataSourcesFactory.fromStringCellRange(
                dataSheet, new CellRangeAddress(firstRow, lastRow, 1, 1));

        XDDFNumericalDataSource<Double> values = XDDFDataSourcesFactory.fromNumericCellRange(
                dataSheet, new CellRangeAddress(firstRow, lastRow, 2, 2));

        XDDFChartData.Series series = chartData.addSeries(dates, values);
        series.setTitle("Комната " + roomNumber, null);

        // Настройка стиля линии
        XDDFLineChartData lineData = (XDDFLineChartData) chartData;
        XDDFLineChartData.Series lineSeries = (XDDFLineChartData.Series) series;
        lineSeries.setSmooth(true);
        lineSeries.setMarkerStyle(MarkerStyle.CIRCLE);

        chart.plot(chartData);

        // Легенда и заголовок
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.TOP_RIGHT);

        chart.setTitleText("Динамика " + getSensorDisplayName(sensorType) + " - Комната " + roomNumber);
        chart.setTitleOverlay(false);

        // Добавляем информационную панель
        createInfoPanel(chartSheet, data, sensorType, roomNumber);
    }

    private static void createInfoPanel(XSSFSheet sheet, List<DynamicMeasurementsDTO> data,
                                        String sensorType, String roomNumber) {
        int startRow = 30; // Начинаем ниже графика

        XSSFRow titleRow = sheet.createRow(startRow);
        titleRow.createCell(0).setCellValue("Статистика для " + getSensorDisplayName(sensorType));

        XSSFRow roomRow = sheet.createRow(startRow + 1);
        roomRow.createCell(0).setCellValue("Комната:");
        roomRow.createCell(1).setCellValue(roomNumber);

        XSSFRow periodRow = sheet.createRow(startRow + 2);
        periodRow.createCell(0).setCellValue("Период:");
        if (!data.isEmpty()) {
            String period = data.get(0).getFormattedDateTime() + " - " +
                    data.get(data.size()-1).getFormattedDateTime();
            periodRow.createCell(1).setCellValue(period);
        }

        XSSFRow countRow = sheet.createRow(startRow + 3);
        countRow.createCell(0).setCellValue("Количество измерений:");
        countRow.createCell(1).setCellValue(data.size());

        // Статистика
        double avg = data.stream()
                .mapToDouble(DynamicMeasurementsDTO::getMeasurementValue)
                .average()
                .orElse(0.0);
        double min = data.stream()
                .mapToDouble(DynamicMeasurementsDTO::getMeasurementValue)
                .min()
                .orElse(0.0);
        double max = data.stream()
                .mapToDouble(DynamicMeasurementsDTO::getMeasurementValue)
                .max()
                .orElse(0.0);

        XSSFRow avgRow = sheet.createRow(startRow + 4);
        avgRow.createCell(0).setCellValue("Среднее значение:");
        avgRow.createCell(1).setCellValue(avg);

        XSSFRow minRow = sheet.createRow(startRow + 5);
        minRow.createCell(0).setCellValue("Минимальное значение:");
        minRow.createCell(1).setCellValue(min);

        XSSFRow maxRow = sheet.createRow(startRow + 6);
        maxRow.createCell(0).setCellValue("Максимальное значение:");
        maxRow.createCell(1).setCellValue(max);
    }

    static String getSensorDisplayName(String sensorType) {
        switch (sensorType.toLowerCase()) {
            case "temp": return "Температура, °C";
            case "humidity": return "Влажность, %";
            case "co2": return "Уровень CO₂, ppm";
            case "pressure": return "Давление, гПа";
            default: return sensorType;
        }
    }
}