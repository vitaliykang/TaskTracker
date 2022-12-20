package com.sunmyoung.task_tracker.controllers.dialogControllers;

import com.sunmyoung.task_tracker.Utilities;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.shape.Rectangle;
import lombok.Getter;

public class PasswordChangeDialogController {
    @FXML
    @Getter
    PasswordField currentPasswordPF, newPasswordPF1, newPasswordPF2;

    @FXML
    Rectangle currentPasswordRectangle, newPasswordRectangle;

    public String getNewPassword() {
        return Utilities.hashPassword(newPasswordPF1.getText());
    }

    public boolean checkNewPassword() {
        return newPasswordPF1.getText().equals(newPasswordPF2.getText());
    }

    public void showCurrentPasswordError(boolean bool) {
        currentPasswordRectangle.setVisible(bool);
    }

    public void showNewPasswordError(boolean bool) {
        newPasswordRectangle.setVisible(bool);
    }
}
