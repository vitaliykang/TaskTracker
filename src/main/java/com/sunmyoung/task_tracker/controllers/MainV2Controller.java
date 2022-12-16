package com.sunmyoung.task_tracker.controllers;

import com.sunmyoung.task_tracker.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;

import javafx.stage.Stage;

import lombok.SneakyThrows;

import static com.sunmyoung.task_tracker.DialogUtilities.centerStage;

public class MainV2Controller {

    @FXML
    @SneakyThrows
    void openArchive(ActionEvent event) {
        Scene scene = createScene("archive.fxml");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();;
        stage.setScene(scene);
        stage.setTitle("Archive");
        centerStage(stage);
        stage.show();
    }

    @FXML
    @SneakyThrows
    void openTVTaskBoard(ActionEvent event) {
        Scene scene = createScene("TVTaskBoard.fxml");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();;
        stage.setScene(scene);
        stage.setTitle("Task Board");
        centerStage(stage);
        stage.show();
    }

    @FXML
    @SneakyThrows
    void openTaskBoard(ActionEvent event) {
        Scene scene = createScene("TaskBoard.fxml");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();;
        stage.setScene(scene);
        stage.setTitle("Task Board");
        centerStage(stage);
        stage.show();
    }

    @FXML
    @SneakyThrows
    void openStock(ActionEvent event) {
        Scene scene = createScene("stock.fxml");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();;
        stage.setScene(scene);
        stage.setTitle("Stock");
        centerStage(stage);
        stage.show();
    }

    @SneakyThrows
    public static void showMainScreen(ActionEvent event) {
        Scene scene = createScene("mainV2.fxml");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();;
        stage.setScene(scene);
        stage.setTitle("Task Tracker");
        centerStage(stage);
        stage.show();
    }

    @SneakyThrows
    private static Scene createScene(String fxml) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml));
        return new Scene(fxmlLoader.load());
    }
}
