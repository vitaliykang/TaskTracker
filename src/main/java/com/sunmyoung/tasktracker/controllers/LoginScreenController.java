package com.sunmyoung.tasktracker.controllers;

import com.sunmyoung.tasktracker.Launcher;
import com.sunmyoung.tasktracker.controllers.settings.ConfigV2;
import com.sunmyoung.tasktracker.repositories.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.kordamp.ikonli.javafx.FontIcon;

public class LoginScreenController {

    @FXML
    private TextField
            addressTF,
            loginTF,
            passwordTF;

    @FXML
    private Button connectButton;

    @FXML
    private FontIcon errorIcon;

    @FXML
    void connect(ActionEvent event) {
        connectToDatabase(event);
    }

    @FXML
    void apply(ActionEvent event) {
        ConfigV2.setCredentials(addressTF.getText(), loginTF.getText(), passwordTF.getText());
        connectButton.setDisable(false);
    }

    public void initialize() {
        addressTF.setText(ConfigV2.getUrl());
        loginTF.setText(ConfigV2.getUsername());
        passwordTF.setText(ConfigV2.getPassword());
    }

    @SneakyThrows
    public void connectToDatabase(ActionEvent event) {
        if (DatabaseConnection.connect()) {
            FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("mainV2.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            ;
            stage.setScene(scene);
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
            stage.show();
        } else {
            errorIcon.setVisible(true);
        }
    }
}
