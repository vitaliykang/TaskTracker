package com.sunmyoung.task_tracker.controllers.dialogControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import lombok.Getter;

public class TestDialogController {
    @FXML
    @Getter
    CheckBox checkBox;

    @FXML
    @Getter
    Rectangle rectangle;

    public void initialize() {
        rectangle.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
           rectangle.setVisible(false);
        });
    }
}
