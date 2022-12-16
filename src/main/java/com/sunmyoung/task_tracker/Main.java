package com.sunmyoung.task_tracker;

import com.sunmyoung.task_tracker.repositories.DatabaseConnection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Getter
    private static final Image logo = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("icon.png")));

    @Override
    @SneakyThrows
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("modeSelectLoginScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initStyle(StageStyle.DECORATED);
        stage.getIcons().add(logo);
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