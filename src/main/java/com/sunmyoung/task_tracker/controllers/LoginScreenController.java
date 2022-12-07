package com.sunmyoung.task_tracker.controllers;

import com.sunmyoung.task_tracker.Main;
import com.sunmyoung.task_tracker.Utilities;
import com.sunmyoung.task_tracker.controllers.settings.SimpleConfig;
import com.sunmyoung.task_tracker.pojos.Client;
import com.sunmyoung.task_tracker.repositories.ClientRepository;
import com.sunmyoung.task_tracker.repositories.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private Button testButton;

    @FXML
    private ImageView animatedImageView;

    private Image animatedLogo;

    @FXML
    @SneakyThrows
    void connect(ActionEvent event) {
        //saving credentials taken from fields into cfg file
        SimpleConfig.saveInfo(addressTF.getText(), loginTF.getText(), passwordTF.getText());
        if (connectToDatabase(event)) {
            Utilities.printStatus("Connected.");
            loadClientList();
        } else {
            Utilities.printStatus("Failed to connect.");
        }
    }

    @FXML
    @SneakyThrows
    void test(ActionEvent event) {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            if (job.printPage(animatedImageView)){
                job.endJob();
            }
        }
    }

    @SneakyThrows
    public void initialize() {
        testButton.setVisible(false);

        addressTF.setText(SimpleConfig.getURL());
        loginTF.setText(SimpleConfig.getUsername());
        passwordTF.setText(SimpleConfig.getPassword());

        String animatedFileName = "animated.gif";
        try {
            animatedLogo = new Image(Objects.requireNonNull(Main.class.getResource(animatedFileName)).toURI().toString());
            animatedImageView.setImage(animatedLogo);
        } catch (NullPointerException e) {
            System.out.println("No logo file found. Name: " + animatedFileName);
        }
    }

    @SneakyThrows
    public boolean connectToDatabase(ActionEvent event) {
        String url = addressTF.getText();
        url = String.format("jdbc:mysql://%s/sunmyoung?useSSL=false", url);
        if (DatabaseConnection.connect(url, loginTF.getText(), passwordTF.getText())) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mainV2.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
            stage.show();
            return true;
        } else {
            errorIcon.setVisible(true);
            animatedImageView.setVisible(false);
            return false;
        }
    }

    private void loadClientList() {
        List<Client> clientList = ClientRepository.findAll();
        List<String> clientStrings = new ArrayList<>();
        clientList.forEach(client -> {
            clientStrings.add(client.convertToString());
        });
        Utilities.writeToFile("data/clients.txt", clientStrings);
        System.out.println("Clients loaded");
    }
}
