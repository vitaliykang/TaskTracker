package com.sunmyoung.tasktracker.controllers.dialogControllers;

import com.sunmyoung.tasktracker.pojos.Subtask;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Getter;

public class CreateSubtaskDialogController {

    @FXML
    @Getter
    private TextField countTF;

    @FXML
    @Getter
    private TextField printTF;

    @FXML
    @Getter
    private TextField thicknessTF;

    public Subtask getSubtask() {
        Subtask subtask = new Subtask();
        subtask.setPrint(printTF.getText());
        subtask.setThickness(thicknessTF.getText());
        subtask.setCount(countTF.getText());
        return subtask;
    }
}
