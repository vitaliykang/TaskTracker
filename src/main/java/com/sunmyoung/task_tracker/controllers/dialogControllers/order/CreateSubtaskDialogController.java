package com.sunmyoung.task_tracker.controllers.dialogControllers.order;

import com.sunmyoung.task_tracker.DialogUtilities;
import com.sunmyoung.task_tracker.pojos.Model;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateSubtaskDialogController {
    @FXML
    @Getter
    private TextField
            countTF,
            printTF,
            thicknessTF;

    @FXML
    private Rectangle
            printHighlight,
            countHighlight,
            thicknessHighlight;

    private Map<TextField, Rectangle> highlightMap = new HashMap<>();

    public void initialize() {
        initHighlight();
    }

    public Model getSubtask() {
        Model subtask = new Model();
        subtask.setPrint(printTF.getText());
        subtask.setThickness(thicknessTF.getText());
        subtask.setCount(countTF.getText());
        return subtask;
    }

    public boolean fieldsOK() {
        return DialogUtilities.checkNonNullFields(highlightMap);
    }

    private void initHighlight() {
        printHighlight.setVisible(false);
        countHighlight.setVisible(false);
        thicknessHighlight.setVisible(false);

        printTF.addEventHandler(KeyEvent.KEY_TYPED, event -> printHighlight.setVisible(false));
        countTF.addEventHandler(KeyEvent.KEY_TYPED, event -> countHighlight.setVisible(false));
        thicknessTF.addEventHandler(KeyEvent.KEY_TYPED, event -> thicknessHighlight.setVisible(false));

        highlightMap.put(printTF, printHighlight);
        highlightMap.put(countTF, countHighlight);
        highlightMap.put(thicknessTF, thicknessHighlight);
    }

}
