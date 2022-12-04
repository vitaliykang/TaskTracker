package com.sunmyoung.task_tracker.controllers.dialogControllers.utility;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import lombok.Getter;

public class ErrorMessageDialogController {
    @FXML
    @Getter
    private TextArea textArea;

    @FXML
    @Getter
    private Label label;
}
