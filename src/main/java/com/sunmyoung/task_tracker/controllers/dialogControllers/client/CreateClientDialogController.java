package com.sunmyoung.task_tracker.controllers.dialogControllers.client;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Getter;

public class CreateClientDialogController {
    @FXML
    @Getter
    private TextField
            companyTF,
            typeTF,
            managerTF,
            codeTF;

}
