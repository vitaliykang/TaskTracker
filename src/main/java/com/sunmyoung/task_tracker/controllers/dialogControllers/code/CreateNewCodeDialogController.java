package com.sunmyoung.task_tracker.controllers.dialogControllers.code;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;

public class CreateNewCodeDialogController {

    @FXML
    @Getter
    private TextField clientTF;

    @FXML
    @Getter
    private TextField codeTF;

    @FXML
    @Getter
    private CheckBox combiCB;

    @FXML
    @Getter
    private TextField frameSizeTF;

    @FXML
    @Getter
    private TextField meshTF;

    @FXML
    @Getter
    private TextField biasTF;

    @FXML
    @Getter
    private TextField tensionTF;

}
