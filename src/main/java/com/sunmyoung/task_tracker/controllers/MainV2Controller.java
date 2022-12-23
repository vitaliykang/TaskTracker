package com.sunmyoung.task_tracker.controllers;

import com.sunmyoung.task_tracker.*;
import com.sunmyoung.task_tracker.controllers.dialogControllers.PasswordChangeDialogController;
import com.sunmyoung.task_tracker.pojos.Password;
import com.sunmyoung.task_tracker.repositories.PasswordRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;

import lombok.SneakyThrows;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Optional;

import static com.sunmyoung.task_tracker.DialogUtilities.centerStage;

public class MainV2Controller {
    @FXML
    void openSettings() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(Dialogs.PASSWORD_CHANGE));
        try {
            Dialog<ButtonType> dialog = new Dialog<>();
            DialogPane dialogPane = fxmlLoader.load();
            dialog.setDialogPane(dialogPane);
            Stage stage = (Stage) dialogPane.getScene().getWindow();
            stage.getIcons().add(Main.getLogo());

            PasswordChangeDialogController controller = fxmlLoader.getController();

            Button buttonOK = (Button) dialogPane.lookupButton(ButtonType.OK);
            buttonOK.addEventFilter(ActionEvent.ACTION, event -> {
                String currentPassword = Utilities.hashPassword(controller.getCurrentPasswordPF().getText());
                String oldPassword = Password.getBufferedPassword() == null ? PasswordRepository.getPassword() : Password.getBufferedPassword();

                controller.showNewPasswordError(false);
                controller.showCurrentPasswordError(false);

                if (currentPassword.equals(oldPassword) && controller.checkNewPassword()) {
                    PasswordRepository.updatePassword(controller.getNewPassword());
                    Password.setBufferedPassword(controller.getNewPassword());

                    dialog.close();
                } else {
                    if (! currentPassword.equals(oldPassword)) {
                        controller.showCurrentPasswordError(true);
                    }
                    if (! controller.checkNewPassword()) {
                        controller.showNewPasswordError(true);
                    }
                    event.consume();
                }
            });

            Optional<ButtonType> result = dialog.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorMessage.show(e);
        }
    }

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
        String taskBoard = Mode.getCurrentMode().getTaskBoard();
        Scene scene = createScene(taskBoard);
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
        Scene scene = createScene(Mode.getCurrentMode().getMainScreen());
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
