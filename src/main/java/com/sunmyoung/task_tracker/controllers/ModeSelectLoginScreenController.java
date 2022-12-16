package com.sunmyoung.task_tracker.controllers;

import com.sunmyoung.task_tracker.*;
import com.sunmyoung.task_tracker.controllers.dialogControllers.SettingsDialogController;
import com.sunmyoung.task_tracker.controllers.settings.SimpleConfig;
import com.sunmyoung.task_tracker.repositories.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Objects;
import java.util.Optional;

public class ModeSelectLoginScreenController {

    @FXML
    private ImageView imageView;

    @FXML
    private FontIcon errorIcon;

    @FXML
    private Button productionBtn;

    @FXML
    private Button okBtn, cancelBtn;

    @FXML
    private PasswordField passwordField;

    private Image logo;
    private final String IMAGE_PATH = "animated.gif";

    @FXML
    void okPressed() {

    }

    @FXML
    void cancelPressed() {
        showPasswordField(false);
    }

    @FXML
    void managementMode(ActionEvent event) {
        showPasswordField(true);
    }

    @FXML
    void openSettings(MouseEvent event) {
        openSettings();
    }

    @FXML
    void productionMode(ActionEvent event) {
        Mode.setCurrentMode(Mode.PRODUCTION);
        connectToDatabase(event);
    }

    public void initialize() {
        initLogo();
    }

    private void initLogo() {
        try {
            logo = new Image(Objects.requireNonNull(Main.class.getResource(IMAGE_PATH)).toURI().toString());
            imageView.setImage(logo);
        } catch (Exception e) {
            Utilities.printStatus("Logo not found.", this.getClass());
            ErrorMessage.show(e);
        }
    }

    private void openSettings() {
        errorIcon.setVisible(false);
        imageView.setVisible(true);

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(Dialogs.SETTINGS));
        Dialog<ButtonType> dialog = new Dialog<>();
        try {
            DialogPane dialogPane = fxmlLoader.load();
            dialog.setDialogPane(dialogPane);
            Stage stage = (Stage) dialogPane.getScene().getWindow();
            stage.getIcons().add(Main.getLogo());
        } catch (Exception e) {
            ErrorMessage.show(e);
            e.printStackTrace();
        }

        SettingsDialogController controller = fxmlLoader.getController();

        controller.getAdressTF().setText(SimpleConfig.getURL());
        controller.getLoginTF().setText(SimpleConfig.getUsername());
        controller.getPasswordTF().setText(SimpleConfig.getPassword());

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.OK)) {
            SimpleConfig.saveInfo(
                    controller.getAdressTF().getText(), controller.getLoginTF().getText(), controller.getPasswordTF().getText());
        }
    }

    private boolean connectToDatabase(ActionEvent event) {
        String url = SimpleConfig.getURL();
        url = String.format("jdbc:mysql://%s/sunmyoung?useSSL=false", url);
        if (DatabaseConnection.connect(url, SimpleConfig.getUsername(), SimpleConfig.getPassword())) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mainV2.fxml"));
            Pane pane = null;
            try {
                pane = fxmlLoader.load();
            } catch (Exception e) {
                ErrorMessage.show(e);
                e.printStackTrace();
            }
            Scene scene = new Scene(pane);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
            stage.show();
            return true;
        } else {
            errorIcon.setVisible(true);
            imageView.setVisible(false);
            return false;
        }
    }

    private void showPasswordField(boolean bool) {
        okBtn.setVisible(bool);
        cancelBtn.setVisible(bool);
        passwordField.setVisible(bool);

        productionBtn.setVisible(!bool);
    }
}
