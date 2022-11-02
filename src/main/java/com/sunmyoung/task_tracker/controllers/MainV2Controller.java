package com.sunmyoung.task_tracker.controllers;

import com.sunmyoung.task_tracker.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.SneakyThrows;

public class MainV2Controller {
    @FXML
    private Button archiveButton;

    @FXML
    private Button taskBoardButton;

    @FXML
    private Button tvTaskBoardButton;

    @FXML
    @SneakyThrows
    void openArchive(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("archive.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();;
        stage.setScene(scene);
        stage.setTitle("Archive");
        centerStage(stage);
        stage.show();
    }

    @FXML
    @SneakyThrows
    void openTVTaskBoard(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("TVTaskBoard.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();;
        stage.setScene(scene);
        stage.setTitle("Task Board");
        centerStage(stage);
        stage.show();
    }

    @FXML
    @SneakyThrows
    void openTaskBoard(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("TaskBoard.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();;
        stage.setScene(scene);
        stage.setTitle("Task Board");
        centerStage(stage);
        stage.show();
    }

    @SneakyThrows
    public static void showMainScreen(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mainV2.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();;
        stage.setScene(scene);
        stage.setTitle("Task Tracker");
        centerStage(stage);
        stage.show();
    }

    private static void centerStage(Stage stage) {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
    }
}
