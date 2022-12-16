package com.sunmyoung.task_tracker.controllers.dialogControllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Getter;

public class SettingsDialogController {

    @FXML
    @Getter
    private TextField
            adressTF,
            loginTF,
            passwordTF;
}
