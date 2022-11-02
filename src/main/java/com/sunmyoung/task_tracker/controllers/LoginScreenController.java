package com.sunmyoung.task_tracker.controllers;

import com.sunmyoung.task_tracker.Main;
import com.sunmyoung.task_tracker.controllers.settings.SimpleConfig;
import com.sunmyoung.task_tracker.repositories.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
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
    private FontIcon
            errorIcon,
            gearsIcon;

    @FXML
    void connect(ActionEvent event) {
        //saving credentials taken from fields into cfg file
        SimpleConfig.saveInfo(addressTF.getText(), loginTF.getText(), passwordTF.getText());
        connectToDatabase(event);
    }

    public void initialize() {
        addressTF.setText(SimpleConfig.getURL());
        loginTF.setText(SimpleConfig.getUsername());
        passwordTF.setText(SimpleConfig.getPassword());
    }

    @SneakyThrows
    public void connectToDatabase(ActionEvent event) {
        String url = addressTF.getText();
        url = String.format("jdbc:mysql://%s/sunmyoung?useSSL=false", url);
        if (DatabaseConnection.connect(url, loginTF.getText(), passwordTF.getText())) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mainV2.fxml"));
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
            gearsIcon.setVisible(false);
        }
    }
}
