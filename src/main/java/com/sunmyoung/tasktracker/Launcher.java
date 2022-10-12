package com.sunmyoung.tasktracker;

import com.sunmyoung.tasktracker.repositories.Database;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.SneakyThrows;

import java.io.IOException;

public class Launcher extends Application {

    @Override
    @SneakyThrows
    public void start(Stage stage) throws IOException {
        Database.connect();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
//        scene.getStylesheets().add(getClass().getResource("fontstyle.css").toExternalForm());
        stage.setTitle("Task Tracker");
        stage.setMinWidth(1900);
        stage.setMinHeight(900);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                Platform.exit();
                System.exit(0);
            }
        });

        stage.show();
    }

    public static void main(String[] args) throws Exception{
        launch();
    }
}