package com.sunmyoung.task_tracker.controllers;

import com.sunmyoung.task_tracker.*;
import com.sunmyoung.task_tracker.controllers.dialogControllers.SettingsDialogController;
import com.sunmyoung.task_tracker.controllers.settings.SimpleConfig;
import com.sunmyoung.task_tracker.pojos.Password;
import com.sunmyoung.task_tracker.repositories.DatabaseConnection;
import com.sunmyoung.task_tracker.repositories.PasswordRepository;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    private Label noConnectionLabel, wrongPasswordLabel;

    @FXML
    private Button okBtn, cancelBtn;

    @FXML
    private PasswordField passwordField;

    private final String IMAGE_PATH = "animated.gif";

    @FXML
    void okPressed(ActionEvent event) {
        if (checkPassword()) {
            Mode.setCurrentMode(Mode.MANAGEMENT);
            loadMainScreen(event);
        } else {
            showWrongPasswordWarning(true);
        }
    }

    @FXML
    void cancelPressed() {
        showPasswordField(false);
        showWrongPasswordWarning(false);
    }

    @FXML
    void managementMode(ActionEvent event) {
        showPasswordField(! passwordField.isVisible());
    }

    @FXML
    void openSettings(MouseEvent event) {
        openSettings();
    }

    @FXML
    void productionMode(ActionEvent event) {
        Mode.setCurrentMode(Mode.PRODUCTION);
        if (connectToDatabase()) {
            loadMainScreen(event);
        }
    }

    @FXML
    void readKeyboard(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            if (checkPassword()) {
                Mode.setCurrentMode(Mode.MANAGEMENT);
                loadMainScreen(event);
            } else {
                showWrongPasswordWarning(true);
            }
        } else if (event.getCode() == KeyCode.P && event.isAltDown() && event.isControlDown()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Do you want to reset the password?");
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                if (DatabaseConnection.getEntityManagerFactory() == null) {
                    connectToDatabase();
                }
                PasswordRepository.resetPassword();
                Utilities.printStatus("Password has been reset.", this.getClass());
            }
        }
    }

    public void initialize() {
        initLogo();
    }

    private void initLogo() {
        try {
            Image logo = new Image(Objects.requireNonNull(Main.class.getResource(IMAGE_PATH)).toURI().toString());
            imageView.setImage(logo);
        } catch (Exception e) {
            Utilities.printStatus("Logo not found.", this.getClass());
            ErrorMessage.show(e);
        }
    }

    private void openSettings() {
        errorIcon.setVisible(false);
        noConnectionLabel.setVisible(false);
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

    private boolean connectToDatabase() {
        String url = SimpleConfig.getURL();
        url = String.format("jdbc:mysql://%s/sunmyoung?useSSL=false", url);
        if (DatabaseConnection.connect(url, SimpleConfig.getUsername(), SimpleConfig.getPassword())) {
            return true;
        } else {
            errorIcon.setVisible(true);
            noConnectionLabel.setVisible(true);
            imageView.setVisible(false);
            return false;
        }
    }
    
    private void loadMainScreen(Event event) {
        String mainScreen = Mode.getCurrentMode() == Mode.MANAGEMENT ? Mode.MANAGEMENT.getMainScreen() : Mode.PRODUCTION.getMainScreen();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(mainScreen));
        Pane pane = null;
        try {
            pane = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorMessage.show(e);
        }
        Scene scene = new Scene(pane);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
        stage.show();
    }

    private void showPasswordField(boolean bool) {
        okBtn.setVisible(bool);
        cancelBtn.setVisible(bool);
        passwordField.setVisible(bool);

        productionBtn.setVisible(!bool);
    }

    private void showWrongPasswordWarning(boolean bool) {
        wrongPasswordLabel.setVisible(bool);
        errorIcon.setVisible(bool);
        imageView.setVisible(! bool);
    }

    private boolean checkPassword() {
        String typedPassword = Utilities.hashPassword(passwordField.getText());

        if (Password.getBufferedPassword() != null) {
            return typedPassword.equals(Password.getBufferedPassword());
        }
        if (connectToDatabase()) {
            String password = PasswordRepository.getPassword();
            Password.setBufferedPassword(password);
            return password.equals(typedPassword);
        }

        return false;
    }
}
