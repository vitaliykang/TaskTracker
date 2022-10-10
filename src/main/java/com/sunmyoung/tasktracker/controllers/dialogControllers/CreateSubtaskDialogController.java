package com.sunmyoung.tasktracker.controllers.dialogControllers;

import com.sunmyoung.tasktracker.pojos.Subtask;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CreateSubtaskDialogController {

    @FXML
    private TextField countTF;

    @FXML
    private TextField printTF;

    @FXML
    private TextField thicknessTF;

    public Subtask getSubtask() {
        Subtask subtask = new Subtask();
        subtask.setPrint(printTF.getText());
        subtask.setThickness(thicknessTF.getText());
        subtask.setCount(countTF.getText());
        return subtask;
    }
}
