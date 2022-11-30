package com.sunmyoung.task_tracker;

import com.sunmyoung.task_tracker.controllers.dialogControllers.utility.ErrorMessageDialogController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import lombok.SneakyThrows;

public class ErrorMessage {
    private static final String ERROR_DIALOG_LOCATION = "dialogs/utility/errorMessageDialog.fxml";

    @SneakyThrows
    public static void show(String message) {
        FXMLLoader fxmlLoader = new FXMLLoader(ErrorMessage.class.getResource(ERROR_DIALOG_LOCATION));
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(fxmlLoader.load());

        ErrorMessageDialogController controller = fxmlLoader.getController();
        controller.getTextArea().setText(message);

        dialog.showAndWait();
    }

    @SneakyThrows
    public static void show(StackTraceElement[] stackTraceElements) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < stackTraceElements.length; i++) {
            builder.append(stackTraceElements[i]);
            builder.append("\n");
        }
        show(builder.toString());
    }
}
