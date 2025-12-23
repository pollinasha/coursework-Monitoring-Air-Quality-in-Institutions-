package com.example.monitoring_quality;

import com.example.monitoring_quality.controller.BuildingsTableViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MonitoringQualityApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MonitoringQualityApplication.class.getResource("buildings-table-view.fxml"));
        Parent parent = fxmlLoader.load();
        BuildingsTableViewController controller = fxmlLoader.getController();
        controller.stage = stage;
        Scene scene = new Scene(parent, 1000, 600);
        stage.setScene(scene);
        stage.show();
    }
}