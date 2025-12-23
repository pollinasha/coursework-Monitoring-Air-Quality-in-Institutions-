package com.example.monitoring_quality.util;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class HelpFullClass {
    public static ImageView byteArrayToImageView(byte[] imageData, double fitWidth, double fitHeight, boolean preserveRatio) {
        if (imageData == null || imageData.length == 0) {
            return null;
        }
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);
            Image image = new Image(inputStream);
            ImageView imageView = new ImageView(image);
            if (fitWidth > 0) {
                imageView.setFitWidth(fitWidth);
            }
            if (fitHeight > 0) {
                imageView.setFitHeight(fitHeight);
            }
            imageView.setPreserveRatio(preserveRatio);
            return imageView;
        } catch (Exception e) {
            System.err.println("Ошибка преобразования byte[] в ImageView: " + e.getMessage());
            return null;
        }
    }

    public static byte[] imageViewToByteArray(ImageView imageView) {
        if (imageView == null || imageView.getImage() == null) {
            return null;
        }
        try {
            Image image = imageView.getImage();
            PixelReader pixelReader = image.getPixelReader();
            if (pixelReader == null) {
                return null;
            }
            int width = (int) image.getWidth();
            int height = (int) image.getHeight();
            WritableImage writableImage = new WritableImage(width, height);
            PixelWriter pixelWriter = writableImage.getPixelWriter();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    pixelWriter.setColor(x, y, pixelReader.getColor(x, y));
                }
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Color color = pixelReader.getColor(x, y);
                    int argb = ((int) (color.getOpacity() * 255) << 24) | ((int) (color.getRed() * 255) << 16) | ((int) (color.getGreen() * 255) << 8) | ((int) (color.getBlue() * 255));
                    bufferedImage.setRGB(x, y, argb);
                }
            }
            ImageIO.write(bufferedImage, "png", outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            System.err.println("Ошибка преобразования ImageView в byte[]: " + e.getMessage());
            return null;
        }
    }

    public static void showModalStage(Stage mainStage, Parent parent) {
        Stage modalStage = new Stage();
        Scene scene = new Scene(parent, 400, 400);
        modalStage.setTitle("Правка");
        modalStage.setScene(scene);
        modalStage.setResizable(false);
        modalStage.initModality(Modality.WINDOW_MODAL);
        modalStage.initOwner(mainStage);
        modalStage.initStyle(StageStyle.TRANSPARENT);
        modalStage.showAndWait();
    }

    public static void showAlert(String entity) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Укажите " + entity);
        alert.setHeaderText(null);
        alert.setContentText("Поле " + entity + " обязательно к заполнению, пожалуйста добавьте значение для сохранения данных.");
        alert.showAndWait();
    }

    public static void showAlert(String entityTitle, String entityText, boolean clear) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (clear) {
            alert.setTitle(entityTitle);
            alert.setHeaderText(null);
            alert.setContentText(entityText);
        } else {
            alert.setTitle("Укажите " + entityTitle);
            alert.setHeaderText(null);
            alert.setContentText("Поле " + entityText + " обязательно к заполнению, пожалуйста добавьте значение для сохранения данных.");
        }
        alert.showAndWait();
    }
}