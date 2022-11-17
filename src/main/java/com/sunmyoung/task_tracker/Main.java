package com.sunmyoung.task_tracker;

import com.sunmyoung.task_tracker.repositories.DatabaseConnection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.SneakyThrows;

import java.io.IOException;

public class Main extends Application {
    @Override
    @SneakyThrows
    public void start(Stage stage) throws IOException {
//        DatabaseConnection.connect();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("loginScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
//        scene.getStylesheets().add(getClass().getResource("TVstyle.css").toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                if (DatabaseConnection.getEntityManagerFactory() != null) {
                    DatabaseConnection.getEntityManagerFactory().close();
                }
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