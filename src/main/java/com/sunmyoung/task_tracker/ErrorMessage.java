package com.sunmyoung.task_tracker;

import com.sunmyoung.task_tracker.controllers.dialogControllers.utility.ErrorMessageDialogController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import lombok.SneakyThrows;

public class ErrorMessage {
    @SneakyThrows
    public static void show(Exception exception) {
        FXMLLoader fxmlLoader = new FXMLLoader(ErrorMessage.class.getResource(Dialogs.ERROR));
        Dialog<ButtonType> dialog = new Dialog<>();
        DialogPane dialogPane = fxmlLoader.load();
        dialog.setDialogPane(dialogPane);
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        stage.getIcons().add(Main.getLogo());

        ErrorMessageDialogController controller = fxmlLoader.getController();

        StringBuilder traceBuilder = new StringBuilder();
        for (StackTraceElement element : exception.getStackTrace()) {
            traceBuilder.append(element.toString());
            traceBuilder.append("\n");
        }

        controller.getLabel().setText(exception.toString());
        controller.getTextArea().setText(traceBuilder.toString());

        dialog.showAndWait();
    }
}
