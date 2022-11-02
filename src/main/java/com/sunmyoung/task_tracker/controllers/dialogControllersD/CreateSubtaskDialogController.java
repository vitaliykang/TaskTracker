package com.sunmyoung.task_tracker.controllers.dialogControllersD;

import com.sunmyoung.task_tracker.pojos.Model;
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

    public Model getSubtask() {
        Model subtask = new Model();
        subtask.setPrint(printTF.getText());
        subtask.setThickness(thicknessTF.getText());
        subtask.setCount(countTF.getText());
        return subtask;
    }
}
